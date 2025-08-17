import re
import feedparser
import requests
from datetime import datetime, timedelta
import os

# Installation instructions:
# pip install feedparser requests

def get_urls_from_file(file_path):
    """Extracts all RssService URLs from the given Kotlin file."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        # Regex to find all URLs within the RssService definitions
        url_pattern = re.compile(r'RssService\(".*?",\s*"(.*?)"')
        return url_pattern.findall(content)
    except FileNotFoundError:
        print(f"Error: The file at {file_path} was not found.")
        return []

def is_feed_outdated(url):
    """Checks if an RSS feed's latest entry is older than 3 days."""
    try:
        # Set a user-agent to avoid being blocked by some servers
        headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'}
        response = requests.get(url, headers=headers, timeout=10)
        response.raise_for_status()  # Raise an exception for bad status codes
        
        feed = feedparser.parse(response.content)
        if not feed.entries:
            print(f"Feed at {url} has no entries. Marking as outdated.")
            return True

        latest_entry = feed.entries[0]
        pub_date_parsed = latest_entry.get('published_parsed') or latest_entry.get('updated_parsed')

        if pub_date_parsed:
            pub_date = datetime(*pub_date_parsed[:6])
            three_days_ago = datetime.now() - timedelta(days=3)
            if pub_date < three_days_ago:
                print(f"Feed at {url} is outdated. Last entry: {pub_date}")
                return True
        else:
            print(f"No publication date found for the latest entry in {url}. Assuming it's not outdated.")
            return False # Assume not outdated if no date is found

    except Exception as e:
        print(f"Failed to fetch or parse feed at {url}: {e}")
        return True  # Mark as outdated if there's any error
    return False

def main():
    # Construct the path to RssData.kt relative to this script
    script_dir = os.path.dirname(__file__)
    file_path = os.path.join(script_dir, '../app/src/main/java/secret/news/club/infrastructure/rss/RssData.kt')
    
    urls = get_urls_from_file(file_path)
    if not urls:
        print("No URLs found to validate.")
        return

    print(f"Found {len(urls)} RSS feeds to validate.")
    outdated_feeds = [url for url in urls if is_feed_outdated(url)]

    if outdated_feeds:
        print(f"\nFound {len(outdated_feeds)} outdated feeds to remove:")
        for url in outdated_feeds:
            print(f"- {url}")

        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()

        new_lines = []
        for line in lines:
            if any(f'"{url}"' in line for url in outdated_feeds):
                print(f"Removing line: {line.strip()}")
                continue
            new_lines.append(line)

        with open(file_path, 'w', encoding='utf-8') as f:
            f.writelines(new_lines)
        print("\nSuccessfully removed outdated feeds from RssData.kt.")
    else:
        print("\nNo outdated feeds found. The file remains unchanged.")

if __name__ == "__main__":
    main()
