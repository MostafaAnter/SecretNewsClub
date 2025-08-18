import re
import feedparser
import requests
from datetime import datetime, timedelta
import os
import json
import csv
from urllib.parse import urlparse, urljoin, quote_plus
import time
import concurrent.futures
from threading import Lock
from dataclasses import dataclass, asdict
from typing import List, Dict, Optional, Tuple, Set
import argparse
from bs4 import BeautifulSoup

# Installation instructions:
# pip install feedparser requests beautifulsoup4 lxml

@dataclass
class FeedInfo:
    name: str
    url: str
    category: str
    language: str
    country: str

@dataclass
class ValidationResult:
    feed_info: FeedInfo
    is_accessible: bool = False
    response_code: Optional[int] = None
    response_time: float = 0.0
    is_valid_feed: bool = False
    has_recent_content: bool = False
    entry_count: int = 0
    latest_entry_date: Optional[datetime] = None
    error_message: Optional[str] = None
    content_type: Optional[str] = None
    feed_title: Optional[str] = None
    feed_description: Optional[str] = None

@dataclass
class DiscoveredFeed:
    title: str
    url: str
    description: str
    language: str
    country: str
    category: str
    source_website: str
    entry_count: int
    latest_entry_date: Optional[datetime]
    discovery_method: str
    class FeedDiscovery:
        def __init__(self, timeout=15):
            self.timeout = timeout
            self.session = requests.Session()
            self.session.headers.update({
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
                'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
                'Accept-Language': 'en-US,en;q=0.5',
                'Accept-Encoding': 'gzip, deflate',
                'DNT': '1',
                'Connection': 'keep-alive',
                'Upgrade-Insecure-Requests': '1'
            })

            # Country-specific news sites and domains
            self.country_domains = {
                'US': [
                    'cnn.com', 'nytimes.com', 'washingtonpost.com', 'usatoday.com',
                    'npr.org', 'pbs.org', 'abc.com', 'nbcnews.com', 'cbsnews.com',
                    'foxnews.com', 'reuters.com', 'apnews.com', 'politico.com',
                    'thehill.com', 'wsj.com', 'bloomberg.com', 'espn.com', 'si.com',
                    'latimes.com', 'nypost.com', 'huffpost.com', 'time.com'
                ],
                'GB': [
                    'bbc.co.uk', 'theguardian.com', 'independent.co.uk', 'telegraph.co.uk',
                    'mirror.co.uk', 'dailymail.co.uk', 'thesun.co.uk', 'skynews.com',
                    'itv.com', 'channel4.com', 'standard.co.uk', 'metro.co.uk',
                    'express.co.uk', 'thetimes.co.uk', 'ft.com'
                ],
                'CA': [
                    'cbc.ca', 'ctvnews.ca', 'globalnews.ca', 'nationalpost.com',
                    'thestar.com', 'theglobeandmail.com', 'macleans.ca', 'citynews.ca',
                    'cp24.com', 'vancouversun.com', 'calgaryherald.com'
                ],
                'AU': [
                    'abc.net.au', 'smh.com.au', 'theage.com.au', 'news.com.au',
                    'theaustralian.com.au', 'heraldsun.com.au', 'adelaidenow.com.au',
                    'couriermail.com.au', 'nine.com.au', 'sbs.com.au', '7news.com.au'
                ],
                'DE': [
                    'spiegel.de', 'bild.de', 'welt.de', 'zeit.de', 'faz.net',
                    'sueddeutsche.de', 'tagesschau.de', 'focus.de', 'stern.de',
                    'handelsblatt.com', 'kicker.de', 'sport1.de', 'n-tv.de'
                ],
                'FR': [
                    'lemonde.fr', 'lefigaro.fr', 'liberation.fr', '20minutes.fr',
                    'leparisien.fr', 'lexpress.fr', 'france24.com', 'francetvinfo.fr',
                    'lequipe.fr', 'rmc.fr', 'europe1.fr', 'bfmtv.com'
                ],
                'IN': [
                    'timesofindia.indiatimes.com', 'hindustantimes.com', 'thehindu.com',
                    'indianexpress.com', 'ndtv.com', 'india.com', 'firstpost.com',
                    'news18.com', 'zee5.com', 'cricbuzz.com', 'espncricinfo.com',
                    'tribuneindia.com', 'deccanherald.com'
                ],
                'BR': [
                    'globo.com', 'uol.com.br', 'folha.uol.com.br', 'estadao.com.br',
                    'ig.com.br', 'terra.com.br', 'r7.com', 'band.uol.com.br',
                    'ge.globo.com', 'lance.com.br', 'cartacapital.com.br'
                ],
                'JP': [
                    'nikkei.com', 'asahi.com', 'yomiuri.co.jp', 'mainichi.jp',
                    'sankei.com', 'nhk.or.jp', 'kyodo.co.jp', 'japantimes.co.jp',
                    'jiji.com', 'tokyo-np.co.jp'
                ],
                'ES': [
                    'elpais.com', 'elmundo.es', 'abc.es', 'lavanguardia.com',
                    'elconfidencial.com', 'publico.es', 'marca.com', 'as.com',
                    'mundodeportivo.com', 'rtve.es', '20minutos.es'
                ],
                'IT': [
                    'repubblica.it', 'corriere.it', 'lastampa.it', 'ilsole24ore.com',
                    'ansa.it', 'rai.it', 'gazzetta.it', 'corrieredellosport.it',
                    'tuttosport.com', 'sky.it', 'quotidiano.net'
                ]
            }

            # Common RSS feed paths
            self.common_rss_paths = [
                '/rss', '/feed', '/rss.xml', '/feed.xml', '/feeds/all.atom.xml',
                '/atom.xml', '/rss/news', '/feeds/news', '/rss/latest',
                '/feeds/latest', '/index.rss', '/news.rss', '/all.rss',
                '/rss/top-stories', '/feeds/homepage', '/rss/world',
                '/feeds/sport', '/rss/sports', '/feeds/technology',
                '/rss/business', '/feeds/entertainment'
            ]
            def discover_feeds_for_country(self, country_code: str, categories: List[str] = None) -> List[DiscoveredFeed]:
        """Discover RSS feeds for a specific country."""
        if categories is None:
            categories = ['news', 'sports']

        discovered_feeds = []

        print(f"\n🔍 Discovering feeds for {country_code}...")

        # Method 1: Check known domains for the country
        if country_code in self.country_domains:
            domain_feeds = self._discover_from_domains(
                self.country_domains[country_code],
                country_code,
                categories
            )
            discovered_feeds.extend(domain_feeds)

        # Method 2: Search for RSS feeds using search engines
        search_feeds = self._discover_via_search(country_code, categories)
        discovered_feeds.extend(search_feeds)

        # Method 3: Discover YouTube RSS feeds
        youtube_feeds = self._discover_youtube_feeds(country_code, categories)
        discovered_feeds.extend(youtube_feeds)

        # Remove duplicates and validate
        unique_feeds = self._deduplicate_feeds(discovered_feeds)
        validated_feeds = self._validate_discovered_feeds(unique_feeds)

        return validated_feeds

    def _discover_from_domains(self, domains: List[str], country_code: str, categories: List[str]) -> List[DiscoveredFeed]:
        """Discover RSS feeds from known domains."""
        discovered_feeds = []

        print(f"  📡 Checking {len(domains)} known domains...")

        for domain in domains[:15]:  # Limit to prevent too many requests
            try:
                # Try common RSS paths
                for path in self.common_rss_paths[:8]:  # Limit paths per domain
                    url = f"https://{domain}{path}"
                    feed = self._check_rss_url(url, domain, country_code, 'news', 'domain_scan')
                    if feed:
                        discovered_feeds.append(feed)

                # Try to find RSS links on the homepage
                homepage_feeds = self._find_rss_links_on_page(f"https://{domain}", country_code)
                discovered_feeds.extend(homepage_feeds[:3])  # Limit feeds per homepage

                time.sleep(0.5)  # Rate limiting

            except Exception as e:
                continue

        return discovered_feeds

    def _discover_via_search(self, country_code: str, categories: List[str]) -> List[DiscoveredFeed]:
        """Discover RSS feeds using search engines."""
        discovered_feeds = []
        country_names = self._get_country_names(country_code)

        print(f"  🔎 Searching for feeds via search engines...")

        for country_name in country_names[:2]:  # Limit country names
            for category in categories[:2]:  # Limit categories
                # Search queries
                queries = [
                    f'"{country_name}" {category} RSS feed site:*.{country_code.lower()}',
                    f'{category} news {country_name} RSS xml',
                ]

                for query in queries:
                    try:
                        search_results = self._search_google(query)
                        for result_url in search_results[:3]:  # Check top 3 results
                            page_feeds = self._find_rss_links_on_page(result_url, country_code)
                            discovered_feeds.extend(page_feeds[:2])  # Limit feeds per page

                        time.sleep(2)  # Rate limiting for search

                    except Exception as e:
                        continue

        return discovered_feeds

    def _discover_youtube_feeds(self, country_code: str, categories: List[str]) -> List[DiscoveredFeed]:
        """Discover YouTube RSS feeds for news channels."""
        discovered_feeds = []

        print(f"  📺 Searching for YouTube news channels...")

        # Predefined YouTube channel mappings for better results
        youtube_channels = {
            'US': [
                ('UCupvZG-5ko_eiXAupbDfxWw', 'CNN'),
                ('UCXIJgqnII2ZOINSWNOGFThA', 'Fox News'),
                ('UCaXkIU1QidjPwiAiafroGxw', 'MSNBC'),
                ('UCeY0bbntWzzVIaj2z3QigXg', 'NBC News'),
                ('UCBi2mrWuNuyYy4gbM6fU18Q', 'ABC News')
            ],
            'GB': [
                ('UC16niRr50-MSBwiO3YDb3RA', 'BBC News'),
                ('UCoMdktPbSTixAyNGwb-UYkQ', 'Sky News'),
                ('UCIRYBXDze5krPDzAEOxFGVA', 'The Guardian'),
                ('UC9-RFgzMIFmIeS1y1-b9Wpw', 'ITV News')
            ],
            'CA': [
                ('UCuFFtG20a-I1_p2L4tJ7p4w', 'CBC News'),
                ('UChLtXXpo4Ge1Rebe_4wBUAw', 'Global News'),
                ('UCi7Zk9baY1tvdlgxIML8l4A', 'CTV News')
            ],
            'AU': [
                ('UCVgA3pr9Qi4B502iG_2gLSA', 'ABC News Australia'),
                ('UC5T7D-Dh1eDGtsAFCuD84rA', '7NEWS Australia'),
                ('UCp33-V_T1aB5o9L4Ym_b4rg', '9 News Australia')
            ],
            'DE': [
                ('UC5NOEUbkLheQcaaRldYW5GA', 'tagesschau'),
                ('UC1JTaVpQhG1L1P4_K2Wde2g', 'DER SPIEGEL'),
                ('UCb_9d2hGGu1b2a_NEg3wXAg', 'BILD')
            ],
            'FR': [
                ('UCCCPCZNChQdGa9EkATeye4g', 'FRANCE 24'),
                ('UCYpRD2-t73_9E0cq5M-OO4A', 'Le Monde'),
                ('UCK7WvGZ3uMyqB5d2-b2rYpA', 'Brut')
            ],
            'IN': [
                ('UCZFMm1mMw0F81Z37aaEzTUA', 'NDTV'),
                ('UCYPvAwZP8pZhSMW8qs7cVCw', 'India Today'),
                ('UC_gUM8rL-Lrg6O3adPW9K1g', 'WION')
            ]
        }

        if country_code in youtube_channels:
            for channel_id, channel_name in youtube_channels[country_code]:
                rss_url = f"https://www.youtube.com/feeds/videos.xml?channel_id={channel_id}"
                feed = self._check_rss_url(
                    rss_url,
                    channel_name,
                    country_code,
                    'news',
                    'youtube_predefined'
                )
                if feed:
                    discovered_feeds.append(feed)

        return discovered_feeds
    def _find_rss_links_on_page(self, url: str, country_code: str) -> List[DiscoveredFeed]:
        """Find RSS feed links on a webpage."""
        discovered_feeds = []

        try:
            response = self.session.get(url, timeout=self.timeout)
            if response.status_code != 200:
                return discovered_feeds

            soup = BeautifulSoup(response.content, 'html.parser')

            # Look for RSS links in <link> tags
            rss_links = soup.find_all('link', {
                'type': ['application/rss+xml', 'application/atom+xml', 'application/rdf+xml']
            })

            for link in rss_links:
                rss_url = link.get('href')
                if rss_url:
                    # Convert relative URLs to absolute
                    if rss_url.startswith('/'):
                        rss_url = urljoin(url, rss_url)
                    elif not rss_url.startswith('http'):
                        continue

                    title = link.get('title', 'RSS Feed')
                    feed = self._check_rss_url(rss_url, title, country_code, 'news', 'html_link')
                    if feed:
                        discovered_feeds.append(feed)

            # Look for RSS links in anchor tags
            rss_anchors = soup.find_all('a', href=re.compile(r'(rss|feed|atom)', re.I))
            for anchor in rss_anchors[:3]:  # Limit to avoid too many requests
                rss_url = anchor.get('href')
                if rss_url:
                    if rss_url.startswith('/'):
                        rss_url = urljoin(url, rss_url)
                    elif not rss_url.startswith('http'):
                        continue

                    title = anchor.get_text(strip=True) or 'RSS Feed'
                    feed = self._check_rss_url(rss_url, title, country_code, 'news', 'anchor_link')
                    if feed:
                        discovered_feeds.append(feed)

        except Exception as e:
            pass

        return discovered_feeds

    def _check_rss_url(self, url: str, title: str, country_code: str, category: str, method: str) -> Optional[DiscoveredFeed]:
        """Check if a URL is a valid RSS feed."""
        try:
            response = self.session.get(url, timeout=self.timeout)
            if response.status_code != 200:
                return None

            feed = feedparser.parse(response.content)

            if not feed.entries and (not hasattr(feed, 'feed') or not feed.feed):
                return None

            # Extract feed information
            feed_title = getattr(feed.feed, 'title', title)
            description = getattr(feed.feed, 'description', '')
            language = getattr(feed.feed, 'language', self._get_language_for_country(country_code))

            latest_entry_date = None
            if feed.entries:
                entry = feed.entries[0]
                pub_date_parsed = (
                        entry.get('published_parsed') or
                        entry.get('updated_parsed')
                )
                if pub_date_parsed:
                    try:
                        latest_entry_date = datetime(*pub_date_parsed[:6])
                    except:
                        pass

            return DiscoveredFeed(
                title=feed_title,
                url=url,
                description=description[:200] + ('...' if len(description) > 200 else ''),
                language=language,
                country=country_code,
                category=category,
                source_website=urlparse(url).netloc,
                entry_count=len(feed.entries),
                latest_entry_date=latest_entry_date,
                discovery_method=method
            )

        except Exception as e:
            return None

    def _search_google(self, query: str) -> List[str]:
        """Search Google and return URLs (simplified version)."""
        try:
            search_url = f"https://www.google.com/search?q={quote_plus(query)}"
            response = self.session.get(search_url, timeout=self.timeout)

            if response.status_code != 200:
                return []

            soup = BeautifulSoup(response.content, 'html.parser')
            urls = []

            # Extract URLs from search results
            for link in soup.find_all('a', href=True):
                href = link['href']
                if href.startswith('/url?q='):
                    # Extract the actual URL from Google's redirect
                    actual_url = href.split('/url?q=')[1].split('&')[0]
                    if actual_url.startswith('http'):
                        urls.append(actual_url)

            return urls[:5]  # Return top 5 results

        except Exception as e:
            return []

    def _deduplicate_feeds(self, feeds: List[DiscoveredFeed]) -> List[DiscoveredFeed]:
        """Remove duplicate feeds based on URL."""
        seen_urls = set()
        unique_feeds = []

        for feed in feeds:
            if feed.url not in seen_urls:
                seen_urls.add(feed.url)
                unique_feeds.append(feed)

        return unique_feeds

    def _validate_discovered_feeds(self, feeds: List[DiscoveredFeed]) -> List[DiscoveredFeed]:
        """Validate discovered feeds to ensure they're working."""
        validated_feeds = []

        print(f"  ✅ Validating {len(feeds)} discovered feeds...")

        for feed in feeds:
            try:
                response = self.session.get(feed.url, timeout=self.timeout)
                if response.status_code == 200:
                    parsed_feed = feedparser.parse(response.content)
                    if parsed_feed.entries or (hasattr(parsed_feed, 'feed') and parsed_feed.feed):
                        validated_feeds.append(feed)
            except:
                continue

        return validated_feeds

    def _get_country_names(self, country_code: str) -> List[str]:
        """Get country names for search queries."""
        country_names = {
            'US': ['United States', 'America', 'USA'],
            'GB': ['United Kingdom', 'Britain', 'UK', 'England'],
            'CA': ['Canada', 'Canadian'],
            'AU': ['Australia', 'Australian'],
            'DE': ['Germany', 'German', 'Deutschland'],
            'FR': ['France', 'French'],
            'IN': ['India', 'Indian'],
            'BR': ['Brazil', 'Brazilian', 'Brasil'],
            'JP': ['Japan', 'Japanese'],
            'ES': ['Spain', 'Spanish', 'España'],
            'IT': ['Italy', 'Italian', 'Italia'],
            'NL': ['Netherlands', 'Dutch', 'Holland'],
            'SE': ['Sweden', 'Swedish'],
            'NO': ['Norway', 'Norwegian'],
            'DK': ['Denmark', 'Danish'],
            'FI': ['Finland', 'Finnish']
        }
        return country_names.get(country_code, [country_code])

    def _get_language_for_country(self, country_code: str) -> str:
        """Get primary language for country."""
        languages = {
            'US': 'en', 'GB': 'en', 'CA': 'en', 'AU': 'en',
            'DE': 'de', 'AT': 'de', 'CH': 'de',
            'FR': 'fr', 'BE': 'fr',
            'ES': 'es', 'MX': 'es', 'AR': 'es',
            'IT': 'it', 'BR': 'pt', 'IN': 'en',
            'JP': 'ja', 'NL': 'nl', 'SE': 'sv',
            'NO': 'no', 'DK': 'da', 'FI': 'fi'
        }
        return languages.get(country_code, 'en')
    class RSSValidator:
        def __init__(self, max_workers=10, timeout=15, days_threshold=7):
            self.max_workers = max_workers
            self.timeout = timeout
            self.days_threshold = days_threshold
            self.session = requests.Session()
            self.session.headers.update({
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
                'Accept': 'application/rss+xml, application/xml, text/xml, application/atom+xml, */*',
                'Accept-Language': 'en-US,en;q=0.9',
                'Accept-Encoding': 'gzip, deflate',
                'Cache-Control': 'no-cache'
            })
            self.print_lock = Lock()

    def get_feeds_from_file(self, file_path: str) -> List[FeedInfo]:
        """Extracts all RssService information from the given Kotlin file."""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            # Enhanced regex to capture all RssService parameters
            rss_pattern = re.compile(
                r'RssService\(\s*"([^"]*)",\s*"([^"]*)",\s*RssCategory\.(\w+),\s*"([^"]*)",\s*"([^"]*)"\s*\)'
            )

            feeds = []
            matches = rss_pattern.findall(content)

            for match in matches:
                name, url, category, language, country = match
                feeds.append(FeedInfo(name, url, category, language, country))

            return feeds

        except FileNotFoundError:
            print(f"Error: The file at {file_path} was not found.")
            return []
        except Exception as e:
            print(f"Error reading file: {e}")
            return []

    def validate_feed(self, feed_info: FeedInfo) -> ValidationResult:
        """Validates a single RSS feed with comprehensive checks."""
        result = ValidationResult(feed_info=feed_info)
        start_time = time.time()

        try:
            # Make HTTP request
            response = self.session.get(
                feed_info.url,
                timeout=self.timeout,
                allow_redirects=True,
                stream=False
            )

            result.response_time = time.time() - start_time
            result.response_code = response.status_code
            result.content_type = response.headers.get('content-type', '')

            if response.status_code != 200:
                result.error_message = f"HTTP {response.status_code}"
                return result

            result.is_accessible = True

            # Parse the feed
            feed = feedparser.parse(response.content)

            # Check if parsing was successful
            if hasattr(feed, 'bozo') and feed.bozo and not feed.entries:
                result.error_message = f"Feed parsing failed: {getattr(feed, 'bozo_exception', 'Unknown error')}"
                return result

            result.is_valid_feed = True
            result.feed_title = getattr(feed.feed, 'title', 'No title')
            result.feed_description = getattr(feed.feed, 'description', 'No description')
            result.entry_count = len(feed.entries)

            # Check for recent content
            if feed.entries:
                latest_entry = feed.entries[0]
                pub_date_parsed = (
                        latest_entry.get('published_parsed') or
                        latest_entry.get('updated_parsed') or
                        latest_entry.get('created_parsed')
                )

                if pub_date_parsed:
                    try:
                        result.latest_entry_date = datetime(*pub_date_parsed[:6])
                        threshold_date = datetime.now() - timedelta(days=self.days_threshold)
                        result.has_recent_content = result.latest_entry_date > threshold_date
                    except (ValueError, TypeError) as e:
                        result.error_message = f"Date parsing error: {e}"
                else:
                    # If no date found, assume it's recent
                    result.has_recent_content = True
            else:
                result.error_message = "No entries found in feed"

        except requests.Timeout:
            result.error_message = "Request timeout"
            result.response_time = self.timeout
        except requests.ConnectionError:
            result.error_message = "Connection error"
            result.response_time = time.time() - start_time
        except requests.RequestException as e:
            result.error_message = f"Request error: {str(e)}"
            result.response_time = time.time() - start_time
        except Exception as e:
            result.error_message = f"Unexpected error: {str(e)}"
            result.response_time = time.time() - start_time

        return result

    def validate_feeds_parallel(self, feeds: List[FeedInfo], show_progress=True) -> List[ValidationResult]:
        """Validates multiple feeds in parallel."""
        results = []
        completed = 0
        total = len(feeds)

        def validate_with_progress(feed_info):
            nonlocal completed
            result = self.validate_feed(feed_info)

            if show_progress:
                with self.print_lock:
                    completed += 1
                    status = "✓" if result.is_accessible and result.is_valid_feed else "✗"
                    print(f"[{completed:3d}/{total:3d}] {status} {feed_info.name[:50]:<50} ({result.response_time:.2f}s)")

            return result

        with concurrent.futures.ThreadPoolExecutor(max_workers=self.max_workers) as executor:
            futures = [executor.submit(validate_with_progress, feed) for feed in feeds]
            for future in concurrent.futures.as_completed(futures):
                results.append(future.result())

        return results
    class ReportGenerator:
        def __init__(self, results: List[ValidationResult]):
            self.results = results
            self.timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    def generate_console_report(self):
        """Generates a comprehensive console report."""
        total = len(self.results)
        accessible = sum(1 for r in self.results if r.is_accessible)
        valid_feeds = sum(1 for r in self.results if r.is_valid_feed)
        recent_content = sum(1 for r in self.results if r.has_recent_content)

        print("\n" + "="*80)
        print(f"RSS FEED VALIDATION REPORT - {self.timestamp}")
        print("="*80)

        print(f"\nOVERALL STATISTICS:")
        print(f"Total feeds tested: {total}")
        print(f"Accessible feeds: {accessible} ({accessible/total*100:.1f}%)")
        print(f"Valid RSS feeds: {valid_feeds} ({valid_feeds/total*100:.1f}%)")
        print(f"Feeds with recent content: {recent_content} ({recent_content/total*100:.1f}%)")

        # Performance statistics
        accessible_results = [r for r in self.results if r.is_accessible]
        if accessible_results:
            avg_time = sum(r.response_time for r in accessible_results) / len(accessible_results)
            max_time = max(r.response_time for r in accessible_results)
            min_time = min(r.response_time for r in accessible_results)
            print(f"\nPERFORMANCE:")
            print(f"Average response time: {avg_time:.2f}s")
            print(f"Fastest response: {min_time:.2f}s")
            print(f"Slowest response: {max_time:.2f}s")

        # Country breakdown
        country_stats = {}
        for result in self.results:
            country = result.feed_info.country
            if country not in country_stats:
                country_stats[country] = {'total': 0, 'working': 0, 'valid': 0, 'recent': 0}

            country_stats[country]['total'] += 1
            if result.is_accessible:
                country_stats[country]['working'] += 1
            if result.is_valid_feed:
                country_stats[country]['valid'] += 1
            if result.has_recent_content:
                country_stats[country]['recent'] += 1

        print(f"\nCOUNTRY BREAKDOWN:")
        for country, stats in sorted(country_stats.items()):
            success_rate = stats['valid'] / stats['total'] * 100 if stats['total'] > 0 else 0
            print(f"{country}: {stats['valid']}/{stats['total']} valid ({success_rate:.1f}%)")

        # Failed feeds
        failed_feeds = [r for r in self.results if not r.is_accessible or not r.is_valid_feed or not r.has_recent_content]
        if failed_feeds:
            print(f"\nPROBLEMATIC FEEDS ({len(failed_feeds)}):")
            for result in failed_feeds:
                issues = []
                if not result.is_accessible:
                    issues.append("not accessible")
                if result.is_accessible and not result.is_valid_feed:
                    issues.append("invalid RSS")
                if result.is_valid_feed and not result.has_recent_content:
                    issues.append("outdated content")

                print(f"  ✗ {result.feed_info.name} ({result.feed_info.country})")
                print(f"    URL: {result.feed_info.url}")
                print(f"    Issues: {', '.join(issues)}")
                if result.error_message:
                    print(f"    Error: {result.error_message}")
                if result.latest_entry_date:
                    print(f"    Latest entry: {result.latest_entry_date.strftime('%Y-%m-%d %H:%M:%S')}")
                print()

    def save_csv_report(self, filename: str):
        """Saves detailed results to CSV file."""
        with open(filename, 'w', newline='', encoding='utf-8') as csvfile:
            fieldnames = [
                'name', 'url', 'category', 'language', 'country',
                'is_accessible', 'response_code', 'response_time', 'is_valid_feed',
                'has_recent_content', 'entry_count', 'latest_entry_date',
                'error_message', 'content_type', 'feed_title'
            ]

            writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
            writer.writeheader()

            for result in self.results:
                row = {
                    'name': result.feed_info.name,
                    'url': result.feed_info.url,
                    'category': result.feed_info.category,
                    'language': result.feed_info.language,
                    'country': result.feed_info.country,
                    'is_accessible': result.is_accessible,
                    'response_code': result.response_code,
                    'response_time': f"{result.response_time:.2f}",
                    'is_valid_feed': result.is_valid_feed,
                    'has_recent_content': result.has_recent_content,
                    'entry_count': result.entry_count,
                    'latest_entry_date': result.latest_entry_date.isoformat() if result.latest_entry_date else '',
                    'error_message': result.error_message or '',
                    'content_type': result.content_type or '',
                    'feed_title': result.feed_title or ''
                }
                writer.writerow(row)

    def save_json_report(self, filename: str):
        """Saves results to JSON file."""
        json_data = {
            'timestamp': self.timestamp,
            'summary': {
                'total_feeds': len(self.results),
                'accessible_feeds': sum(1 for r in self.results if r.is_accessible),
                'valid_feeds': sum(1 for r in self.results if r.is_valid_feed),
                'recent_content_feeds': sum(1 for r in self.results if r.has_recent_content)
            },
            'results': []
        }

        for result in self.results:
            result_dict = asdict(result)
            # Convert datetime to string for JSON serialization
            if result_dict['latest_entry_date']:
                result_dict['latest_entry_date'] = result.latest_entry_date.isoformat()
            json_data['results'].append(result_dict)

        with open(filename, 'w', encoding='utf-8') as f:
            json.dump(json_data, f, indent=2, ensure_ascii=False)
            class DiscoveryReportGenerator:
    def __init__(self, discovered_feeds: List[DiscoveredFeed]):
        self.discovered_feeds = discovered_feeds
        self.timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    def generate_console_report(self):
        """Generate console report for discovered feeds."""
        if not self.discovered_feeds:
            print("\n❌ No new feeds discovered.")
            return

        print(f"\n🎉 DISCOVERED {len(self.discovered_feeds)} NEW FEEDS")
        print("=" * 60)

        # Group by country
        by_country = {}
        for feed in self.discovered_feeds:
            if feed.country not in by_country:
                by_country[feed.country] = []
            by_country[feed.country].append(feed)

        for country, feeds in by_country.items():
            print(f"\n{country} ({len(feeds)} feeds):")
            for feed in feeds:
                print(f"  📰 {feed.title}")
                print(f"      URL: {feed.url}")
                print(f"      Category: {feed.category}")
                print(f"      Entries: {feed.entry_count}")
                if feed.latest_entry_date:
                    print(f"      Latest: {feed.latest_entry_date.strftime('%Y-%m-%d')}")
                print(f"      Method: {feed.discovery_method}")
                print()

    def generate_kotlin_code(self, country_code: str) -> str:
        """Generate Kotlin code for discovered feeds."""
        country_feeds = [f for f in self.discovered_feeds if f.country == country_code]

        if not country_feeds:
            return f"// No new feeds discovered for {country_code}"

        kotlin_code = f"// Discovered feeds for {country_code} - {self.timestamp}\n"
        kotlin_code += f"// Add these to your get{country_code}RssServices() function:\n\n"

        for feed in country_feeds:
            # Clean up the title for use as service name
            clean_title = re.sub(r'[^\w\s]', '', feed.title)
            clean_title = ' '.join(clean_title.split())

            kotlin_code += f'RssService("{clean_title}", "{feed.url}", RssCategory.{feed.category.upper()}, "{feed.language}", "{feed.country}"),\n'

        return kotlin_code

    def save_discovery_report(self, filename: str):
        """Save discovery report to JSON file."""
        report_data = {
            'timestamp': self.timestamp,
            'total_discovered': len(self.discovered_feeds),
            'feeds': []
        }

        for feed in self.discovered_feeds:
            feed_dict = asdict(feed)
            if feed_dict['latest_entry_date']:
                feed_dict['latest_entry_date'] = feed.latest_entry_date.isoformat()
            report_data['feeds'].append(feed_dict)

        with open(filename, 'w', encoding='utf-8') as f:
            json.dump(report_data, f, indent=2, ensure_ascii=False)

class KotlinFileUpdater:
    def __init__(self, file_path: str):
        self.file_path = file_path

    def add_discovered_feeds_to_file(self, discovered_feeds: List[DiscoveredFeed]):
        """Add discovered feeds to the correct country functions in the Kotlin file."""
        if not discovered_feeds:
            print("No feeds to add to Kotlin file.")
            return

        try:
            with open(self.file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            # Group feeds by country
            feeds_by_country = {}
            for feed in discovered_feeds:
                country = feed.country
                if country not in feeds_by_country:
                    feeds_by_country[country] = []
                feeds_by_country[country].append(feed)

            # Create backup
            backup_path = f"{self.file_path}.backup.{datetime.now().strftime('%Y%m%d_%H%M%S')}"
            with open(backup_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"Backup created: {backup_path}")

            # Update content for each country
            updated_content = content
            for country_code, feeds in feeds_by_country.items():
                updated_content = self._add_feeds_to_country_function(updated_content, country_code, feeds)

            # Write updated content
            with open(self.file_path, 'w', encoding='utf-8') as f:
                f.write(updated_content)

            total_added = sum(len(feeds) for feeds in feeds_by_country.values())
            print(f"Successfully added {total_added} new feeds to {self.file_path}")

        except Exception as e:
            print(f"Error updating Kotlin file: {e}")

    def _add_feeds_to_country_function(self, content: str, country_code: str, feeds: List[DiscoveredFeed]) -> str:
        """Add feeds to the specific country function."""
        # Find the country function
        function_patterns = [
            f'private fun get{country_code}RssServices\\(\\): List<RssService> = listOf\\(',
            f'private fun get{country_code.title()}RssServices\\(\\): List<RssService> = listOf\\(',
        ]

        # Special cases for country function names
        function_name_map = {
            'US': 'getUSRssServices',
            'GB': 'getUKRssServices',
            'UK': 'getUKRssServices'
        }

        if country_code in function_name_map:
            function_patterns.append(f'private fun {function_name_map[country_code]}\\(\\): List<RssService> = listOf\\(')

        function_start = -1
        for pattern in function_patterns:
            match = re.search(pattern, content)
            if match:
                function_start = match.end()
                break

        if function_start == -1:
            print(f"Warning: Could not find function for country {country_code}")
            return content

        # Find the end of the function (closing parenthesis and bracket)
        paren_count = 1
        i = function_start
        while i < len(content) and paren_count > 0:
            if content[i] == '(':
                paren_count += 1
            elif content[i] == ')':
                paren_count -= 1
            i += 1

        if paren_count > 0:
            print(f"Warning: Could not find end of function for country {country_code}")
            return content

        function_end = i - 1  # Position of the closing parenthesis

        # Find the last RssService entry to insert after it
        function_content = content[function_start:function_end]

        # Look for the last RssService entry
        last_service_match = None
        for match in re.finditer(r'RssService\([^)]+\)', function_content):
            last_service_match = match

        if last_service_match:
            insert_position = function_start + last_service_match.end()

            # Generate new RSS service entries
            new_entries = []
            for feed in feeds:
                # Clean up the title for use as service name
                clean_title = re.sub(r'[^\w\s-]', '', feed.title)
                clean_title = ' '.join(clean_title.split())
                if len(clean_title) > 50:
                    clean_title = clean_title[:50] + "..."

                # Determine category section
                category = feed.category.upper()
                if category not in ['NEWS', 'SPORTS']:
                    category = 'NEWS'  # Default to NEWS

                new_entry = f',\n    RssService("{clean_title}", "{feed.url}", RssCategory.{category}, "{feed.language}", "{feed.country}")'
                new_entries.append(new_entry)

            # Insert the new entries
            new_content = (
                    content[:insert_position] +
                    ''.join(new_entries) +
                    content[insert_position:]
            )

            print(f"Added {len(feeds)} feeds to {country_code} function")
            return new_content
        else:
            print(f"Warning: Could not find existing RssService entries for country {country_code}")
            return content

    def remove_problematic_feeds(self, results: List[ValidationResult], criteria: str = 'strict'):
        """Remove problematic feeds from the Kotlin file."""
        feeds_to_remove = []

        for result in results:
            should_remove = False

            if criteria == 'strict':
                should_remove = (not result.is_accessible or
                                 not result.is_valid_feed or
                                 not result.has_recent_content)
            elif criteria == 'moderate':
                should_remove = (not result.is_accessible or
                                 not result.is_valid_feed)
            elif criteria == 'loose':
                should_remove = not result.is_accessible

            if should_remove:
                feeds_to_remove.append(result.feed_info.url)

        if not feeds_to_remove:
            print(f"No feeds to remove based on '{criteria}' criteria.")
            return

        print(f"\nRemoving {len(feeds_to_remove)} feeds based on '{criteria}' criteria:")
        for url in feeds_to_remove:
            print(f"  - {url}")

        # Create backup
        backup_path = f"{self.file_path}.backup.{datetime.now().strftime('%Y%m%d_%H%M%S')}"
        with open(self.file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        with open(backup_path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"\nBackup created: {backup_path}")

        # Remove problematic feeds
        lines = content.split('\n')
        new_lines = []
        removed_count = 0

        for line in lines:
            should_remove_line = False
            for url in feeds_to_remove:
                if f'"{url}"' in line:
                    should_remove_line = True
                    removed_count += 1
                    break

            if not should_remove_line:
                new_lines.append(line)

        # Write updated content
        with open(self.file_path, 'w', encoding='utf-8') as f:
            f.write('\n'.join(new_lines))

        print(f"Successfully removed {removed_count} feed entries from {self.file_path}")
        def main():
    parser = argparse.ArgumentParser(description='RSS Feed Validator and Discovery Tool')
    parser.add_argument('--file', '-f',
                        help='Path to RssData.kt file',
                        default='../app/src/main/java/secret/news/club/infrastructure/rss/RssData.kt')
    parser.add_argument('--workers', '-w', type=int, default=10,
                        help='Number of parallel workers (default: 10)')
    parser.add_argument('--timeout', '-t', type=int, default=15,
                        help='Request timeout in seconds (default: 15)')
    parser.add_argument('--days', '-d', type=int, default=7,
                        help='Days threshold for recent content (default: 7)')
    parser.add_argument('--remove', choices=['strict', 'moderate', 'loose'],
                        help='Remove problematic feeds (strict/moderate/loose)')
    parser.add_argument('--discover', '-D', nargs='+',
                        help='Discover new feeds for countries (e.g., --discover US GB CA)')
    parser.add_argument('--discover-all', action='store_true',
                        help='Discover feeds for all supported countries')
    parser.add_argument('--categories', nargs='+',
                        default=['news', 'sports'],
                        help='Categories to discover (default: news sports)')
    parser.add_argument('--no-progress', action='store_true',
                        help='Disable progress output during validation')
    parser.add_argument('--output-dir', '-o', default='.',
                        help='Output directory for reports (default: current directory)')
    parser.add_argument('--generate-kotlin', action='store_true',
                        help='Generate Kotlin code for discovered feeds')
    parser.add_argument('--add-to-file', action='store_true',
                        help='Automatically add discovered feeds to Kotlin file')

    args = parser.parse_args()

    # Construct file path
    script_dir = os.path.dirname(__file__)
    file_path = os.path.join(script_dir, args.file)

    # Initialize validator and discovery
    validator = RSSValidator(
        max_workers=args.workers,
        timeout=args.timeout,
        days_threshold=args.days
    )

    discovery = FeedDiscovery(timeout=args.timeout)

    # Handle feed discovery
    if args.discover or args.discover_all:
        countries_to_discover = []

        if args.discover_all:
            countries_to_discover = ['US', 'GB', 'CA', 'AU', 'DE', 'FR', 'IN', 'BR', 'JP', 'ES', 'IT']
        elif args.discover:
            countries_to_discover = [c.upper() for c in args.discover]

        all_discovered_feeds = []

        for country in countries_to_discover:
            print(f"\n🌍 Discovering feeds for {country}...")
            discovered_feeds = discovery.discover_feeds_for_country(country, args.categories)
            all_discovered_feeds.extend(discovered_feeds)

            if discovered_feeds:
                print(f"✅ Found {len(discovered_feeds)} feeds for {country}")
            else:
                print(f"❌ No feeds found for {country}")

        # Generate discovery report
        discovery_report = DiscoveryReportGenerator(all_discovered_feeds)
        discovery_report.generate_console_report()

        # Save discovery report
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        discovery_file = os.path.join(args.output_dir, f"discovered_feeds_{timestamp}.json")
        discovery_report.save_discovery_report(discovery_file)
        print(f"\n📁 Discovery report saved: {discovery_file}")

        # Generate Kotlin code if requested
        if args.generate_kotlin and all_discovered_feeds:
            kotlin_dir = os.path.join(args.output_dir, "kotlin_feeds")
            os.makedirs(kotlin_dir, exist_ok=True)

            for country in countries_to_discover:
                kotlin_code = discovery_report.generate_kotlin_code(country)
                kotlin_file = os.path.join(kotlin_dir, f"{country}_discovered_feeds.kt")

                with open(kotlin_file, 'w', encoding='utf-8') as f:
                    f.write(kotlin_code)

                print(f"📝 Kotlin code generated: {kotlin_file}")

        # Add feeds to Kotlin file if requested
        if args.add_to_file and all_discovered_feeds and os.path.exists(file_path):
            updater = KotlinFileUpdater(file_path)
            updater.add_discovered_feeds_to_file(all_discovered_feeds)

        return

    # Original validation functionality
    if not os.path.exists(file_path):
        print(f"Error: File not found at {file_path}")
        return

    # Extract and validate existing feeds
    print(f"Extracting feeds from {file_path}...")
    feeds = validator.get_feeds_from_file(file_path)

    if not feeds:
        print("No feeds found to validate.")
        return

    print(f"Found {len(feeds)} RSS feeds to validate.")
    print(f"Using {args.workers} workers with {args.timeout}s timeout.")
    print(f"Content recency threshold: {args.days} days.")
    print()

    # Validate feeds
    results = validator.validate_feeds_parallel(feeds, show_progress=not args.no_progress)

    # Generate reports
    report_gen = ReportGenerator(results)
    report_gen.generate_console_report()

    # Save detailed reports
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    csv_file = os.path.join(args.output_dir, f"rss_validation_{timestamp}.csv")
    json_file = os.path.join(args.output_dir, f"rss_validation_{timestamp}.json")

    report_gen.save_csv_report(csv_file)
    report_gen.save_json_report(json_file)

    print(f"\nDetailed reports saved:")
    print(f"  CSV: {csv_file}")
    print(f"  JSON: {json_file}")

    # Remove problematic feeds if requested
    if args.remove:
        updater = KotlinFileUpdater(file_path)
        updater.remove_problematic_feeds(results, args.remove)

if __name__ == "__main__":
    main()