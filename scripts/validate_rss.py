#!/usr/bin/env python3
"""
RSS Feed Validator and Discovery Tool — async rewrite.

Drop-in replacement: same CLI flags, same Gradle tasks, faster via asyncio+aiohttp.
Discovery uses Google HTML scraping (Chrome UA, no API key) with DuckDuckGo as fallback.
"""
from __future__ import annotations

import re
import asyncio
import aiohttp
import feedparser
import os
import json
import csv
import argparse
import time
from datetime import datetime, timedelta
from dataclasses import dataclass, asdict
from typing import Optional
from urllib.parse import urljoin, quote_plus
from bs4 import BeautifulSoup


# ── Config ────────────────────────────────────────────────────────────────────

DEFAULT_KOTLIN_FILE = os.path.join(
    os.path.dirname(__file__),
    '../app/src/main/java/secret/news/club/infrastructure/rss/RssData.kt',
)

COMMON_RSS_PATHS = [
    '/rss', '/feed', '/rss.xml', '/feed.xml', '/atom.xml',
    '/feeds/all.atom.xml', '/rss/news', '/feeds/news',
    '/index.rss', '/news.rss', '/rss/top-stories',
]

FETCH_HEADERS = {
    'User-Agent': 'Mozilla/5.0 (compatible; SecretNewsBot/2.0)',
    'Accept': 'application/rss+xml, application/atom+xml, application/xml, text/xml, */*',
    'Accept-Language': 'en-US,en;q=0.9',
}

# Chrome-like headers for Google search scraping (mimics a real browser)
GOOGLE_HEADERS = {
    'User-Agent': (
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) '
        'AppleWebKit/537.36 (KHTML, like Gecko) '
        'Chrome/124.0.0.0 Safari/537.36'
    ),
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'en-US,en;q=0.9',
    'Accept-Encoding': 'gzip, deflate, br',
}

# Created lazily so it binds to the running event loop at first use
_SEARCH_SEM: 'asyncio.Semaphore | None' = None


def _get_search_sem() -> asyncio.Semaphore:
    global _SEARCH_SEM
    if _SEARCH_SEM is None:
        _SEARCH_SEM = asyncio.Semaphore(3)
    return _SEARCH_SEM

# Single source of truth for all country data.
# 'fn' must match the exact function name in RssData.kt.
COUNTRIES: dict = {
    'US': {
        'language': 'en', 'full_name': 'US', 'fn': 'getUSRssServices',
        'domains': ['cnn.com', 'nytimes.com', 'npr.org', 'apnews.com', 'reuters.com',
                    'espn.com', 'foxnews.com', 'nbcnews.com', 'bloomberg.com', 'wsj.com'],
        'youtube': [('UCupvZG-5ko_eiXAupbDfxWw', 'CNN'), ('UCXIJgqnII2ZOINSWNOGFThA', 'Fox News'),
                    ('UCeY0bbntWzzVIaj2z3QigXg', 'NBC News'), ('UCBi2mrWuNuyYy4gbM6fU18Q', 'ABC News')],
    },
    'GB': {
        'language': 'en', 'full_name': 'UK', 'fn': 'getUKRssServices',
        'domains': ['bbc.co.uk', 'theguardian.com', 'independent.co.uk',
                    'telegraph.co.uk', 'skynews.com', 'ft.com', 'dailymail.co.uk'],
        'youtube': [('UC16niRr50-MSBwiO3YDb3RA', 'BBC News'), ('UCoMdktPbSTixAyNGwb-UYkQ', 'Sky News'),
                    ('UCIRYBXDze5krPDzAEOxFGVA', 'The Guardian')],
    },
    'CA': {
        'language': 'en', 'full_name': 'Canada', 'fn': 'getCanadaRssServices',
        'domains': ['cbc.ca', 'ctvnews.ca', 'globalnews.ca', 'nationalpost.com',
                    'thestar.com', 'theglobeandmail.com', 'macleans.ca'],
        'youtube': [('UCuFFtG20a-I1_p2L4tJ7p4w', 'CBC News'), ('UChLtXXpo4Ge1Rebe_4wBUAw', 'Global News')],
    },
    'AU': {
        'language': 'en', 'full_name': 'Australia', 'fn': 'getAustraliaRssServices',
        'domains': ['abc.net.au', 'smh.com.au', 'news.com.au', 'sbs.com.au', '7news.com.au'],
        'youtube': [('UCVgA3pr9Qi4B502iG_2gLSA', 'ABC News Australia'),
                    ('UC5T7D-Dh1eDGtsAFCuD84rA', '7NEWS Australia')],
    },
    'DE': {
        'language': 'de', 'full_name': 'Germany', 'fn': 'getGermanyRssServices',
        'domains': ['spiegel.de', 'bild.de', 'welt.de', 'zeit.de', 'faz.net',
                    'sueddeutsche.de', 'tagesschau.de', 'focus.de', 'n-tv.de',
                    'rss.dw.com', 'heise.de', 'ndr.de', 'zdf.de', 'mdr.de', 'tagesspiegel.de'],
        'youtube': [('UC5NOEUbkLheQcaaRldYW5GA', 'tagesschau'), ('UC1JTaVpQhG1L1P4_K2Wde2g', 'DER SPIEGEL')],
    },
    'FR': {
        'language': 'fr', 'full_name': 'France', 'fn': 'getFranceRssServices',
        'domains': ['lemonde.fr', 'lefigaro.fr', 'liberation.fr', 'leparisien.fr',
                    'france24.com', 'francetvinfo.fr', 'lequipe.fr', 'bfmtv.com'],
        'youtube': [('UCCCPCZNChQdGa9EkATeye4g', 'FRANCE 24'), ('UCYpRD2-t73_9E0cq5M-OO4A', 'Le Monde')],
    },
    'IN': {
        'language': 'en', 'full_name': 'India', 'fn': 'getIndiaRssServices',
        'domains': ['timesofindia.indiatimes.com', 'hindustantimes.com', 'thehindu.com',
                    'indianexpress.com', 'ndtv.com', 'firstpost.com', 'news18.com'],
        'youtube': [('UCZFMm1mMw0F81Z37aaEzTUA', 'NDTV'), ('UCYPvAwZP8pZhSMW8qs7cVCw', 'India Today'),
                    ('UC_gUM8rL-Lrg6O3adPW9K1g', 'WION')],
    },
    'BR': {
        'language': 'pt', 'full_name': 'Brazil', 'fn': 'getBrazilRssServices',
        'domains': ['globo.com', 'uol.com.br', 'folha.uol.com.br', 'estadao.com.br', 'r7.com',
                    'metropoles.com', 'agenciabrasil.ebc.com.br', 'veja.abril.com.br', 'exame.com'],
        'youtube': [],
    },
    'JP': {
        'language': 'ja', 'full_name': 'Japan', 'fn': 'getJapanRssServices',
        'domains': ['nikkei.com', 'asahi.com', 'yomiuri.co.jp', 'nhk.or.jp', 'japantimes.co.jp',
                    'kyodonews.net', 'english.kyodonews.net', 'mainichi.jp', 'the-japan-news.com'],
        'youtube': [],
    },
    'ES': {
        'language': 'es', 'full_name': 'Spain', 'fn': 'getSpainRssServices',
        'domains': ['elpais.com', 'elmundo.es', 'lavanguardia.com', 'marca.com', 'rtve.es'],
        'youtube': [],
    },
    'IT': {
        'language': 'it', 'full_name': 'Italy', 'fn': 'getItalyRssServices',
        'domains': ['repubblica.it', 'corriere.it', 'ansa.it', 'rai.it', 'gazzetta.it'],
        'youtube': [],
    },
    'EG': {
        'language': 'ar', 'full_name': 'Egypt', 'fn': 'getEgyptRssServices',
        'domains': ['ahram.org.eg', 'youm7.com', 'masrawy.com', 'filgoal.com', 'yallakora.com'],
        'youtube': [('UCb2pc3QkNBd6XhJ4h2x4_wQ', 'Al Jazeera Mubasher'),
                    ('UC-4KnwVftfg4A0I4-iL5W8g', 'BBC News عربي'),
                    ('UCTXf0-8X22eG6L2viGf7o_A', 'Sky News Arabia')],
    },
    'MX': {
        'language': 'es', 'full_name': 'Mexico', 'fn': 'getMexicoRssServices',
        'domains': ['milenio.com', 'excelsior.com.mx', 'eluniversal.com.mx', 'mediotiempo.com',
                    'jornada.com.mx', 'proceso.com.mx', 'animalpolitico.com', 'record.com.mx'],
        'youtube': [],
    },
    'RU': {
        'language': 'ru', 'full_name': 'Russia', 'fn': 'getRussiaRssServices',
        'domains': ['ria.ru', 'tass.ru', 'kommersant.ru', 'iz.ru', 'lenta.ru'],
        'youtube': [],
    },
    'ZA': {
        'language': 'en', 'full_name': 'SouthAfrica', 'fn': 'getSouthAfricaRssServices',
        'domains': ['news24.com', 'timeslive.co.za', 'ewn.co.za', 'sabcnews.com',
                    'dailymaverick.co.za', 'groundup.org.za', 'businesslive.co.za', 'mg.co.za'],
        'youtube': [],
    },
    'AE': {
        'language': 'ar', 'full_name': 'Uae', 'fn': 'getUaeRssServices',
        'domains': ['gulfnews.com', 'khaleejtimes.com', 'thenationalnews.com', 'arabianbusiness.com'],
        'youtube': [],
    },
    'TR': {
        'language': 'tr', 'full_name': 'Turkey', 'fn': 'getTurkeyRssServices',
        'domains': ['hurriyet.com.tr', 'sabah.com.tr', 'haberturk.com', 'cumhuriyet.com.tr'],
        'youtube': [],
    },
    'AR': {
        'language': 'es', 'full_name': 'Argentina', 'fn': 'getArgentinaRssServices',
        'domains': ['clarin.com', 'lanacion.com.ar', 'infobae.com', 'ole.com.ar',
                    'eldestapeweb.com', 'pagina12.com.ar', 'tycsports.com', 'ambito.com'],
        'youtube': [],
    },
    'NG': {
        'language': 'en', 'full_name': 'Nigeria', 'fn': 'getNigeriaRssServices',
        'domains': ['punchng.com', 'vanguardngr.com', 'guardian.ng', 'premiumtimesng.com'],
        'youtube': [],
    },
    'KE': {
        'language': 'en', 'full_name': 'Kenya', 'fn': 'getKenyaRssServices',
        'domains': ['standardmedia.co.ke', 'nation.africa', 'citizen.digital',
                    'the-star.co.ke', 'businessdailyafrica.com', 'capitalfm.co.ke',
                    'kenyans.co.ke', 'tuko.co.ke'],
        'youtube': [],
    },
    'DK': {
        'language': 'da', 'full_name': 'Denmark', 'fn': 'getDenmarkRssServices',
        'domains': ['dr.dk', 'politiken.dk', 'berlingske.dk', 'bt.dk',
                    'tv2.dk', 'altinget.dk', 'borsen.dk', 'zetland.dk'],
        'youtube': [],
    },
    'FI': {
        'language': 'fi', 'full_name': 'Finland', 'fn': 'getFinlandRssServices',
        'domains': ['yle.fi', 'hs.fi', 'is.fi', 'iltalehti.fi', 'kauppalehti.fi',
                    'ts.fi', 'mtvuutiset.fi', 'verkkouutiset.fi'],
        'youtube': [],
    },
    'BE': {
        'language': 'nl', 'full_name': 'Belgium', 'fn': 'getBelgiumRssServices',
        'domains': ['hln.be', 'nieuwsblad.be', 'standaard.be', 'rtbf.be', 'lesoir.be'],
        'youtube': [],
    },
    'CH': {
        'language': 'de', 'full_name': 'Switzerland', 'fn': 'getSwitzerlandRssServices',
        'domains': ['nzz.ch', 'blick.ch', 'tagesanzeiger.ch', 'srf.ch'],
        'youtube': [],
    },
    'PL': {
        'language': 'pl', 'full_name': 'Poland', 'fn': 'getPolandRssServices',
        'domains': ['onet.pl', 'wp.pl', 'gazeta.pl', 'tvn24.pl', 'polsatnews.pl'],
        'youtube': [],
    },
    'HU': {
        'language': 'hu', 'full_name': 'Hungary', 'fn': 'getHungaryRssServices',
        'domains': ['index.hu', 'origo.hu', '444.hu', 'hvg.hu'],
        'youtube': [],
    },
    'GR': {
        'language': 'el', 'full_name': 'Greece', 'fn': 'getGreeceRssServices',
        'domains': ['in.gr', 'protothema.gr', 'kathimerini.gr', 'sport24.gr'],
        'youtube': [],
    },
    'CN': {
        'language': 'zh', 'full_name': 'China', 'fn': 'getChinaRssServices',
        'domains': ['chinadaily.com.cn', 'globaltimes.cn', 'supchina.com', 'sixthtone.com',
                    'xinhuanet.com', 'cgtn.com', 'scmp.com'],
        'youtube': [],
    },
    'NL': {
        'language': 'nl', 'full_name': 'Netherlands', 'fn': 'getNetherlandsRssServices',
        'domains': ['nu.nl', 'telegraaf.nl', 'nos.nl', 'volkskrant.nl'],
        'youtube': [],
    },
    'SE': {
        'language': 'sv', 'full_name': 'Sweden', 'fn': 'getSwedenRssServices',
        'domains': ['svt.se', 'aftonbladet.se', 'dn.se', 'expressen.se'],
        'youtube': [],
    },
    'NO': {
        'language': 'no', 'full_name': 'Norway', 'fn': 'getNorwayRssServices',
        'domains': ['nrk.no', 'vg.no', 'dagbladet.no', 'aftenposten.no'],
        'youtube': [],
    },
    'SA': {
        'language': 'ar', 'full_name': 'Saudi Arabia', 'fn': 'getSaudiArabiaRssServices',
        'domains': ['arabnews.com', 'saudigazette.com.sa', 'alarabiya.net',
                    'okaz.com.sa', 'aawsat.com', 'asharqnews.com'],
        'youtube': [],
    },
    'AT': {
        'language': 'de', 'full_name': 'Austria', 'fn': 'getAustriaRssServices',
        'domains': ['orf.at', 'derstandard.at', 'krone.at', 'kurier.at',
                    'diepresse.com', 'heute.at', 'kleinezeitung.at', 'oe24.at'],
        'youtube': [],
    },
    'CZ': {
        'language': 'cs', 'full_name': 'Czech Republic', 'fn': 'getCzechRepublicRssServices',
        'domains': ['ct24.ceskatelevize.cz', 'idnes.cz', 'blesk.cz', 'aktualne.cz',
                    'novinky.cz', 'lidovky.cz', 'e15.cz', 'irozhlas.cz'],
        'youtube': [],
    },
}


# ── Data classes ──────────────────────────────────────────────────────────────

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
    feed_title: Optional[str] = None


# ── Internal helpers ──────────────────────────────────────────────────────────

def _parse_feed(content: bytes) -> tuple:
    """
    Returns (is_valid, title, entry_count, latest_date).
    Runs feedparser synchronously — it's CPU-bound, not I/O-bound.
    """
    parsed = feedparser.parse(content)
    has_entries = bool(parsed.entries)
    has_meta = hasattr(parsed, 'feed') and bool(parsed.feed)
    if not has_entries and not has_meta:
        return False, None, 0, None

    title = getattr(getattr(parsed, 'feed', None), 'title', None)
    count = len(parsed.entries)
    latest = None
    if parsed.entries:
        tup = parsed.entries[0].get('published_parsed') or parsed.entries[0].get('updated_parsed')
        if tup:
            try:
                latest = datetime(*tup[:6])
            except (ValueError, TypeError):
                pass
    return True, title, count, latest


def _rss_links_from_html(content: bytes, base_url: str) -> list:
    """Extract RSS/Atom feed URLs from an HTML page."""
    soup = BeautifulSoup(content, 'lxml')
    urls = []
    # <link type="…"> tags are always reliable feed declarations
    for tag in soup.find_all('link', type=re.compile(r'rss|atom|rdf', re.I)):
        href = tag.get('href', '')
        if href:
            urls.append(urljoin(base_url, href))
    # <a> tags — only include hrefs that actually look like feed files
    _FEED_ENDS = ('.rss', '.xml', '.atom', '/feed', '/rss', '/atom',
                  '/feed.xml', '/rss.xml', '/atom.xml', '/feeds')
    for tag in soup.find_all('a', href=True):
        href = tag['href']
        path = href.lower().split('?')[0].split('#')[0]
        if any(path.endswith(s) for s in _FEED_ENDS):
            if href.startswith('http'):
                urls.append(href)
            elif href.startswith('/'):
                urls.append(urljoin(base_url, href))
    return list(dict.fromkeys(urls))  # dedupe, preserve order


# ── Validation ────────────────────────────────────────────────────────────────

async def _validate_one(session: aiohttp.ClientSession, feed: FeedInfo,
                         timeout: int, days: int) -> ValidationResult:
    result = ValidationResult(feed_info=feed)
    t0 = time.monotonic()
    try:
        async with session.get(
            feed.url,
            timeout=aiohttp.ClientTimeout(total=timeout),
            allow_redirects=True,
        ) as resp:
            result.response_time = time.monotonic() - t0
            result.response_code = resp.status
            if resp.status != 200:
                result.error_message = f'HTTP {resp.status}'
                return result
            result.is_accessible = True
            content = await resp.read()
    except asyncio.TimeoutError:
        result.error_message = 'Timeout'
        result.response_time = timeout
        return result
    except aiohttp.ClientError as exc:
        result.error_message = f'{type(exc).__name__}'
        result.response_time = time.monotonic() - t0
        return result

    is_valid, title, count, latest = _parse_feed(content)
    if not is_valid:
        result.error_message = 'Not a valid RSS/Atom feed'
        return result

    result.is_valid_feed = True
    result.feed_title = title
    result.entry_count = count
    result.latest_entry_date = latest

    if latest:
        result.has_recent_content = latest > datetime.now() - timedelta(days=days)
    elif count > 0:
        result.has_recent_content = True   # no date info → assume fresh
    else:
        result.error_message = 'Feed has no entries'

    return result


async def validate_all(feeds: list, workers: int, timeout: int, days: int,
                        show_progress: bool = True) -> list:
    sem = asyncio.Semaphore(workers)
    completed = 0
    connector = aiohttp.TCPConnector(limit=workers, limit_per_host=3)

    async with aiohttp.ClientSession(connector=connector, headers=FETCH_HEADERS) as session:
        async def bounded(feed):
            nonlocal completed
            async with sem:
                r = await _validate_one(session, feed, timeout, days)
            completed += 1
            if show_progress:
                sym = '✓' if r.is_valid_feed else '✗'
                print(f'[{completed:3d}/{len(feeds):3d}] {sym} {feed.name[:55]:<55} ({r.response_time:.1f}s)')
            return r

        return list(await asyncio.gather(*[bounded(f) for f in feeds]))


# ── Discovery ─────────────────────────────────────────────────────────────────

async def _probe_feed_url(session: aiohttp.ClientSession, url: str, timeout: int,
                           country: str, hint_name: str, category: str,
                           method: str) -> Optional[FeedInfo]:
    """Fetch url once; return FeedInfo only if it's a working feed."""
    try:
        async with session.get(
            url, timeout=aiohttp.ClientTimeout(total=timeout), allow_redirects=True
        ) as resp:
            if resp.status != 200:
                return None
            content = await resp.read()
    except Exception:
        return None

    is_valid, title, _, _ = _parse_feed(content)
    if not is_valid:
        return None

    lang = COUNTRIES.get(country, {}).get('language', 'en')
    name = (title or hint_name)[:80]
    return FeedInfo(name=name, url=url, category=category, language=lang, country=country)


async def _discover_domain(session: aiohttp.ClientSession, domain: str,
                            country: str, timeout: int) -> list:
    base = f'https://{domain}'
    found = []

    # Probe common RSS paths
    path_results = await asyncio.gather(
        *[_probe_feed_url(session, f'{base}{p}', timeout, country, domain, 'NEWS', 'path_scan')
          for p in COMMON_RSS_PATHS],
        return_exceptions=True,
    )
    found.extend(r for r in path_results if isinstance(r, FeedInfo))

    # Scrape homepage for embedded RSS links
    try:
        async with session.get(base, timeout=aiohttp.ClientTimeout(total=timeout)) as resp:
            if resp.status == 200:
                html = await resp.read()
                rss_urls = _rss_links_from_html(html, base)
                link_results = await asyncio.gather(
                    *[_probe_feed_url(session, u, timeout, country, domain, 'NEWS', 'html_link')
                      for u in rss_urls[:6]],
                    return_exceptions=True,
                )
                found.extend(r for r in link_results if isinstance(r, FeedInfo))
    except Exception:
        pass

    return found


async def _discover_youtube(session: aiohttp.ClientSession, country: str, timeout: int) -> list:
    channels = COUNTRIES.get(country, {}).get('youtube', [])
    if not channels:
        return []
    results = await asyncio.gather(
        *[_probe_feed_url(
            session,
            f'https://www.youtube.com/feeds/videos.xml?channel_id={cid}',
            timeout, country, name, 'NEWS', 'youtube',
          ) for cid, name in channels],
        return_exceptions=True,
    )
    return [r for r in results if isinstance(r, FeedInfo)]


async def _google_search_async(session: aiohttp.ClientSession, query: str,
                               num: int = 8) -> list:
    """
    Async Google HTML scraping with Chrome UA — no API key, timeouts work reliably.
    Returns a list of result page URLs.
    """
    url = f'https://www.google.com/search?q={quote_plus(query)}&num={num}&hl=en&gl=us'
    try:
        async with session.get(
            url,
            headers=GOOGLE_HEADERS,
            timeout=aiohttp.ClientTimeout(total=15),
            allow_redirects=True,
        ) as resp:
            if resp.status != 200:
                return []
            html = await resp.read()
    except Exception:
        return []

    soup = BeautifulSoup(html, 'lxml')
    urls = []
    for a in soup.find_all('a', href=True):
        href = a['href']
        if href.startswith('/url?q='):
            actual = href.split('/url?q=')[1].split('&')[0]
            if actual.startswith('http') and 'google.com' not in actual:
                urls.append(actual)
    return urls[:num]


async def _discover_search(session: aiohttp.ClientSession, country: str,
                            categories: list, timeout: int) -> list:
    """
    Async Google HTML search (Chrome UA, no API key) to find news RSS feeds.
    All I/O is async — timeouts are guaranteed by aiohttp, no threads involved.
    """
    country_name = COUNTRIES.get(country, {}).get('full_name', country)

    # Diverse queries targeting actual RSS feed URLs
    queries: list = []
    for cat in categories:
        queries.append(f'{country_name} {cat} news site RSS feed')
        queries.append(f'site:{country.lower()} {cat} news RSS OR feed.xml OR atom.xml')
    queries.append(f'{country_name} news RSS feeds')
    queries.append(f'top news {country_name} RSS')

    page_urls: list = []
    async with _get_search_sem():
        for i, q in enumerate(queries[:4], 1):
            print(f'  [{country}] search {i}/4: "{q[:60]}"...', flush=True)
            urls = await _google_search_async(session, q)
            if not urls:
                print(f'  [{country}] no results (blocked/CAPTCHA), skipping query', flush=True)
            page_urls.extend(urls)
            await asyncio.sleep(2)  # polite pause between queries

    found = []
    seen: set = set()
    for page_url in list(dict.fromkeys(page_urls))[:12]:
        try:
            async with session.get(
                page_url, timeout=aiohttp.ClientTimeout(total=timeout),
            ) as resp:
                if resp.status != 200:
                    continue
                html = await resp.read()
            rss_urls = [u for u in _rss_links_from_html(html, page_url) if u not in seen]
            results = await asyncio.gather(
                *[_probe_feed_url(session, u, timeout, country, page_url, 'NEWS', 'search')
                  for u in rss_urls[:5]],
                return_exceptions=True,
            )
            for r in results:
                if isinstance(r, FeedInfo):
                    found.append(r)
                    seen.add(r.url)
        except Exception:
            continue

    return found


async def discover_country(country: str, categories: list, timeout: int, workers: int) -> list:
    cfg = COUNTRIES.get(country)
    if not cfg:
        print(f'  Unknown country: {country} (add it to COUNTRIES dict)')
        return []

    print(f'\n  [{country}] scanning domains, YouTube, search...')
    connector = aiohttp.TCPConnector(limit=workers, limit_per_host=2)

    async with aiohttp.ClientSession(connector=connector, headers=FETCH_HEADERS) as session:
        domain_results, yt_results, search_results = await asyncio.gather(
            asyncio.gather(*[_discover_domain(session, d, country, timeout)
                             for d in cfg['domains']], return_exceptions=True),
            _discover_youtube(session, country, timeout),
            _discover_search(session, country, categories, timeout),
        )

    all_feeds: list = []
    for r in domain_results:
        if isinstance(r, list):
            all_feeds.extend(r)
    all_feeds.extend(yt_results)
    all_feeds.extend(search_results)

    # Deduplicate by URL
    seen: set = set()
    unique = [f for f in all_feeds if not (f.url in seen or seen.add(f.url))]  # type: ignore[func-returns-value]

    print(f'  [{country}] found {len(unique)} feeds')
    return unique


# ── Kotlin file manipulation ──────────────────────────────────────────────────

_RSS_RE = re.compile(
    r'RssService\(\s*"([^"]*)"\s*,\s*"([^"]*)"\s*,\s*RssCategory\.(\w+)\s*,'
    r'\s*"([^"]*)"\s*,\s*"([^"]*)"\s*\)'
)


def read_feeds_from_kotlin(path: str) -> list:
    with open(path, encoding='utf-8') as f:
        content = f.read()
    return [FeedInfo(name, url, cat, lang, country)
            for name, url, cat, lang, country in _RSS_RE.findall(content)]


def _backup(path: str) -> None:
    stamp = datetime.now().strftime('%Y%m%d_%H%M%S')
    backup_path = f'{path}.backup.{stamp}'
    with open(path, encoding='utf-8') as f:
        data = f.read()
    with open(backup_path, 'w', encoding='utf-8') as f:
        f.write(data)
    print(f'  Backup: {backup_path}')


def add_feeds_to_kotlin(path: str, feeds: list) -> None:
    if not feeds:
        return

    with open(path, encoding='utf-8') as f:
        content = f.read()

    # Collect all URLs already present in the file to avoid duplicates
    existing_urls = {url for _, url, _, _, _ in _RSS_RE.findall(content)}
    original_count = len(feeds)
    feeds = [f for f in feeds if f.url not in existing_urls]
    skipped = original_count - len(feeds)
    if skipped:
        print(f'  Skipped {skipped} already-present URLs ({len(feeds)} new feeds to add)')
    if not feeds:
        print('  All discovered feeds already exist in the file, nothing to add.')
        return

    _backup(path)

    by_country: dict = {}
    for feed in feeds:
        by_country.setdefault(feed.country, []).append(feed)

    updated = content
    for country, country_feeds in by_country.items():
        fn = COUNTRIES.get(country, {}).get('fn')
        if not fn:
            print(f'  Warning: no function name for {country}, add it to COUNTRIES dict')
            continue

        fn_pattern = re.compile(
            rf'private fun {re.escape(fn)}\(\)\s*:\s*List<RssService>\s*=\s*listOf\('
        )
        m = fn_pattern.search(updated)
        if not m:
            print(f'  Warning: {fn}() not found in {path}')
            continue

        # Walk forward from listOf( to find the matching closing paren
        i, depth = m.end(), 1
        while i < len(updated) and depth > 0:
            if updated[i] == '(':
                depth += 1
            elif updated[i] == ')':
                depth -= 1
            i += 1
        closing = i - 1

        # Find the last existing RssService entry before closing
        last_match = None
        for match in _RSS_RE.finditer(updated[m.end():closing]):
            last_match = match
        if last_match is None:
            print(f'  Warning: {fn}() has no existing entries, skipping')
            continue

        insert_at = m.end() + last_match.end()
        new_entries = ''.join(
            f',\n    RssService("{f.name}", "{f.url}", RssCategory.{f.category}, '
            f'"{f.language}", "{f.country}")'
            for f in country_feeds
        )
        updated = updated[:insert_at] + new_entries + updated[insert_at:]
        print(f'  Added {len(country_feeds)} feeds to {fn}()')

    with open(path, 'w', encoding='utf-8') as f:
        f.write(updated)


def remove_feeds_from_kotlin(path: str, urls: list) -> None:
    if not urls:
        print('No feeds to remove.')
        return

    _backup(path)
    url_set = set(urls)

    with open(path, encoding='utf-8') as f:
        lines = f.readlines()

    kept = [l for l in lines if not any(f'"{u}"' in l for u in url_set)]
    removed = len(lines) - len(kept)

    with open(path, 'w', encoding='utf-8') as f:
        f.writelines(kept)

    print(f'Removed {removed} feed line(s) from {path}')


def generate_kotlin_code(feeds: list, country: str) -> str:
    fn = COUNTRIES.get(country, {}).get('fn', f'get{country}RssServices')
    country_feeds = [f for f in feeds if f.country == country]
    if not country_feeds:
        return f'// No feeds discovered for {country}'
    lines = [f'// Discovered feeds for {country} — add to {fn}():']
    for f in country_feeds:
        lines.append(
            f'RssService("{f.name}", "{f.url}", RssCategory.{f.category}, '
            f'"{f.language}", "{f.country}"),'
        )
    return '\n'.join(lines)


# ── Reports ───────────────────────────────────────────────────────────────────

def print_validation_report(results: list) -> None:
    total = len(results)
    accessible = sum(1 for r in results if r.is_accessible)
    valid = sum(1 for r in results if r.is_valid_feed)
    recent = sum(1 for r in results if r.has_recent_content)

    print(f'\n{"=" * 72}')
    print(f'RSS VALIDATION — {datetime.now().strftime("%Y-%m-%d %H:%M")}')
    print(f'{"=" * 72}')
    print(f'Total: {total}  Accessible: {accessible}  Valid: {valid}  Recent: {recent}')

    accessible_results = [r for r in results if r.is_accessible]
    if accessible_results:
        times = [r.response_time for r in accessible_results]
        print(f'Response time: avg {sum(times)/len(times):.1f}s  '
              f'min {min(times):.1f}s  max {max(times):.1f}s')

    by_country: dict = {}
    for r in results:
        s = by_country.setdefault(r.feed_info.country, {'total': 0, 'valid': 0})
        s['total'] += 1
        if r.is_valid_feed:
            s['valid'] += 1

    print('\nCountry breakdown:')
    for c, s in sorted(by_country.items()):
        pct = s['valid'] / s['total'] * 100
        print(f'  {c}: {s["valid"]}/{s["total"]} ({pct:.0f}%)')

    failed = [r for r in results if not r.is_valid_feed or not r.has_recent_content]
    if failed:
        print(f'\nProblematic feeds ({len(failed)}):')
        for r in failed:
            issues = []
            if not r.is_accessible:
                issues.append('unreachable')
            elif not r.is_valid_feed:
                issues.append('invalid feed')
            elif not r.has_recent_content:
                date_str = r.latest_entry_date.strftime('%Y-%m-%d') if r.latest_entry_date else 'no date'
                issues.append(f'stale ({date_str})')
            print(f'  ✗ {r.feed_info.name} ({r.feed_info.country}) — {", ".join(issues)}')
            if r.error_message:
                print(f'      {r.error_message}')


def save_reports(results: list, output_dir: str) -> None:
    ts = datetime.now().strftime('%Y%m%d_%H%M%S')
    csv_path = os.path.join(output_dir, f'rss_validation_{ts}.csv')
    json_path = os.path.join(output_dir, f'rss_validation_{ts}.json')

    fields = ['name', 'url', 'country', 'language', 'category', 'is_accessible',
              'response_code', 'response_time', 'is_valid_feed', 'has_recent_content',
              'entry_count', 'latest_entry_date', 'error_message', 'feed_title']
    with open(csv_path, 'w', newline='', encoding='utf-8') as f:
        w = csv.DictWriter(f, fieldnames=fields)
        w.writeheader()
        for r in results:
            w.writerow({
                'name': r.feed_info.name, 'url': r.feed_info.url,
                'country': r.feed_info.country, 'language': r.feed_info.language,
                'category': r.feed_info.category,
                'is_accessible': r.is_accessible, 'response_code': r.response_code,
                'response_time': f'{r.response_time:.2f}', 'is_valid_feed': r.is_valid_feed,
                'has_recent_content': r.has_recent_content, 'entry_count': r.entry_count,
                'latest_entry_date': r.latest_entry_date.isoformat() if r.latest_entry_date else '',
                'error_message': r.error_message or '', 'feed_title': r.feed_title or '',
            })

    json_results = []
    for r in results:
        d = asdict(r)
        if d.get('latest_entry_date'):
            d['latest_entry_date'] = r.latest_entry_date.isoformat()
        json_results.append(d)

    with open(json_path, 'w', encoding='utf-8') as f:
        json.dump({
            'timestamp': ts,
            'summary': {'total': len(results), 'valid': sum(1 for r in results if r.is_valid_feed),
                        'recent': sum(1 for r in results if r.has_recent_content)},
            'results': json_results,
        }, f, indent=2, ensure_ascii=False)

    print(f'\nReports saved:\n  CSV:  {csv_path}\n  JSON: {json_path}')


def save_discovery_report(feeds: list, output_dir: str) -> str:
    ts = datetime.now().strftime('%Y%m%d_%H%M%S')
    path = os.path.join(output_dir, f'discovered_feeds_{ts}.json')
    with open(path, 'w', encoding='utf-8') as f:
        json.dump({'timestamp': ts, 'total': len(feeds),
                   'feeds': [asdict(fd) for fd in feeds]}, f, indent=2, ensure_ascii=False)
    print(f'Discovery report: {path}')
    return path


# ── Entry point ───────────────────────────────────────────────────────────────

def main() -> None:
    parser = argparse.ArgumentParser(description='RSS Feed Validator and Discovery Tool')
    parser.add_argument('--file', '-f', default=DEFAULT_KOTLIN_FILE,
                        help='Path to RssData.kt')
    parser.add_argument('--workers', '-w', type=int, default=10,
                        help='Concurrent request limit (default: 10)')
    parser.add_argument('--timeout', '-t', type=int, default=15,
                        help='Per-request timeout in seconds (default: 15)')
    parser.add_argument('--days', '-d', type=int, default=7,
                        help='Recency threshold in days (default: 7)')
    parser.add_argument('--remove', choices=['strict', 'moderate', 'loose'],
                        help='Remove feeds: strict=unreachable+invalid+stale, '
                             'moderate=unreachable+invalid, loose=unreachable only')
    parser.add_argument('--discover', '-D', nargs='+', metavar='CC',
                        help='Discover feeds for country codes (e.g. US GB CA)')
    parser.add_argument('--discover-all', action='store_true',
                        help='Discover for all countries in COUNTRIES dict')
    parser.add_argument('--categories', nargs='+', default=['news', 'sports'],
                        help='Discovery categories (default: news sports)')
    parser.add_argument('--no-progress', action='store_true',
                        help='Suppress per-feed progress lines')
    parser.add_argument('--output-dir', '-o', default='.',
                        help='Directory for saved reports (default: current dir)')
    parser.add_argument('--generate-kotlin', action='store_true',
                        help='Write per-country Kotlin snippets to output-dir/kotlin_feeds/')
    parser.add_argument('--add-to-file', action='store_true',
                        help='Insert discovered feeds directly into RssData.kt')
    parser.add_argument('--input-file', '-i',
                        help='JSON discovery report to add (skips network discovery)')
    args = parser.parse_args()

    file_path = os.path.abspath(args.file)

    # ── Discovery mode ────────────────────────────────────────────────────────
    if args.discover or args.discover_all or args.input_file:
        if args.input_file:
            print(f'Loading feeds from {args.input_file}...')
            with open(args.input_file, encoding='utf-8') as f:
                data = json.load(f)
            all_feeds = [FeedInfo(**fd) for fd in data['feeds']]
        else:
            countries = (list(COUNTRIES.keys()) if args.discover_all
                         else [c.upper() for c in (args.discover or [])])
            print(f'Discovering feeds for: {", ".join(countries)}')

            async def run_discovery():
                total = len(countries)
                done = 0
                all_feeds: list = []
                tasks = {
                    asyncio.ensure_future(
                        discover_country(c, args.categories, args.timeout, args.workers)
                    ): c
                    for c in countries
                }
                for fut in asyncio.as_completed(tasks):
                    feeds = await fut
                    done += 1
                    all_feeds.extend(feeds)
                    print(f'\n>>> [{done}/{total} countries done] '
                          f'total feeds so far: {len(all_feeds)}', flush=True)
                return all_feeds

            all_feeds = asyncio.run(run_discovery())

        if not all_feeds:
            print('No feeds discovered.')
            return

        by_country: dict = {}
        for f in all_feeds:
            by_country.setdefault(f.country, []).append(f)
        print(f'\nDiscovered {len(all_feeds)} feeds total:')
        for c, feeds in sorted(by_country.items()):
            print(f'  {c}: {len(feeds)}')

        save_discovery_report(all_feeds, args.output_dir)

        if args.generate_kotlin:
            kt_dir = os.path.join(args.output_dir, 'kotlin_feeds')
            os.makedirs(kt_dir, exist_ok=True)
            for country in {f.country for f in all_feeds}:
                code = generate_kotlin_code(all_feeds, country)
                kt_path = os.path.join(kt_dir, f'{country}_feeds.kt')
                with open(kt_path, 'w', encoding='utf-8') as f:
                    f.write(code)
                print(f'  Kotlin snippet: {kt_path}')

        if args.add_to_file:
            if os.path.exists(file_path):
                add_feeds_to_kotlin(file_path, all_feeds)
            else:
                print(f'Warning: {file_path} not found, skipping --add-to-file')
        return

    # ── Validation mode ───────────────────────────────────────────────────────
    if not os.path.exists(file_path):
        print(f'Error: {file_path} not found')
        return

    feeds = read_feeds_from_kotlin(file_path)
    if not feeds:
        print('No RssService entries found in file.')
        return

    print(f'Validating {len(feeds)} feeds ({args.workers} workers, {args.timeout}s timeout)...\n')
    results = asyncio.run(
        validate_all(feeds, args.workers, args.timeout, args.days,
                     show_progress=not args.no_progress)
    )

    print_validation_report(results)
    save_reports(results, args.output_dir)

    if args.remove:
        criteria = args.remove
        to_remove = []
        for r in results:
            if criteria == 'strict' and (not r.is_accessible or not r.is_valid_feed or not r.has_recent_content):
                to_remove.append(r.feed_info.url)
            elif criteria == 'moderate' and (not r.is_accessible or not r.is_valid_feed):
                to_remove.append(r.feed_info.url)
            elif criteria == 'loose' and not r.is_accessible:
                to_remove.append(r.feed_info.url)

        if to_remove:
            print(f'\nRemoving {len(to_remove)} feeds ({criteria})...')
            remove_feeds_from_kotlin(file_path, to_remove)
        else:
            print(f'\nNo feeds to remove under "{criteria}" criteria.')


if __name__ == '__main__':
    main()