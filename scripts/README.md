# RSS Feed Management System

A comprehensive Python-based tool for validating existing RSS feeds and discovering new ones, integrated with Gradle for easy automation.

## 📋 Table of Contents

- [Overview](#overview)
- [Setup & Installation](#setup--installation)
- [Available Commands](#available-commands)
- [Command Categories](#command-categories)
- [Usage Examples](#usage-examples)
- [Configuration Options](#configuration-options)
- [Best Practices](#best-practices)
- [Troubleshooting](#troubleshooting)
- [Adding a New Country](#adding-a-new-country)

## 🔧 Overview

This system provides automated RSS feed management capabilities:

- **Validation**: Test existing feeds for accessibility, validity, and recent content
- **Discovery**: Automatically find new RSS feeds from major news sources
- **Integration**: Seamlessly add discovered feeds to your Kotlin project
- **Cleanup**: Remove broken or outdated feeds automatically
- **Reporting**: Generate detailed reports in multiple formats

## 🚀 Setup & Installation

### Prerequisites

- Python 3.7+
- Gradle
- Internet connection for feed discovery

### Installation

1. **Dependencies are automatically managed** - The Gradle tasks will:
    - Create a Python virtual environment
    - Install required packages from `requirements.txt`
    - Run the validation/discovery scripts

2. **Manual Setup** (optional):
   ```bash
   cd scripts
   python3 -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   pip install -r requirements.txt

## Direct Script Usage (Python)

This section explains how to run the `validate_rss.py` script directly using Python. This is useful for debugging, development, or running outside the Gradle environment.

### Running the script

First, ensure you have activated the virtual environment:
```bash
source venv/bin/activate # On Windows: venv\Scripts\activate
```

### Available Arguments

The script offers several command-line arguments for customization:

| Argument | Short | Description | Default |
|---|---|---|---|
| `--help` | `-h` | Show help message and exit | N/A |
| `--file` | `-f` | Path to the `RssData.kt` file to validate. | `../app/src/main/java/secret/news/club/infrastructure/rss/RssData.kt` |
| `--workers` | `-w` | Number of parallel workers for validation. | `10` |
| `--timeout` | `-t` | Request timeout in seconds. | `15` |
| `--days` | `-d` | Days threshold for recent content. | `7` |
| `--remove` | | Remove problematic feeds from the Kotlin file. Options: `strict`, `moderate`, `loose`. | `None` |
| `--discover` | `-D` | Discover new feeds for a list of countries (e.g., `US GB`). | `None` |
| `--discover-all` | | Discover feeds for all supported countries. | `False` |
| `--categories` | | Categories to discover (e.g., `news sports`). | `news sports` |
| `--no-progress`| | Disable the progress bar during validation. | `False` |
| `--output-dir` | `-o` | Directory to save reports. | Current directory |
| `--generate-kotlin` | | Generate Kotlin code snippets for discovered feeds. | `False` |
| `--add-to-file` | | Automatically add discovered feeds to the Kotlin file. | `False` |

### Usage Examples

*   **Basic Validation:**
    ```bash
    python scripts/validate_rss.py
    ```

*   **Discover Feeds for Specific Countries:**
    ```bash
    python scripts/validate_rss.py --discover US GB --categories news
    ```

*   **Discover and Automatically Add Feeds:**
    ```bash
    python scripts/validate_rss.py --discover-all --add-to-file
    ```

*   **Remove Broken Feeds (Strict Mode):**
    ```bash
    python scripts/validate_rss.py --remove strict
    ```

## � Available Commands

### 🔍 Basic Operations

| Command | Description | Usage |
|---------|-------------|-------|
| `validateRssFeeds` | Validate existing RSS feeds | `./gradlew :scripts:validateRssFeeds` |
| `rssHelp` | Show all available commands | `./gradlew :scripts:rssHelp` |

### 🌍 Discovery Commands

| Command | Description | Usage |
|---------|-------------|-------|
| `discoverRssFeeds` | Discover feeds for specific countries | `./gradlew :scripts:discoverRssFeeds -Pcountries=US,GB,CA` |
| `discoverAllRssFeeds` | Discover feeds for all supported countries | `./gradlew :scripts:discoverAllRssFeeds` |
| `discoverAndAddFeeds` | Discover and auto-add to Kotlin file | `./gradlew :scripts:discoverAndAddFeeds -Pcountries=US,GB,CA` |

### 🧹 Cleanup Commands

| Command | Description | Usage |
|---------|-------------|-------|
| `cleanupRssFeeds` | Remove problematic feeds | `./gradlew :scripts:cleanupRssFeeds -Premove=moderate` |

### 🚀 Regional Discovery

| Command | Description | Countries Included |
|---------|-------------|-------------------|
| `discoverMajorCountries` | English-speaking countries | US, GB, CA, AU |
| `discoverEuropeanFeeds` | European countries | GB, DE, FR, IT, ES, NL, SE, NO, DK, FI |
| `discoverAsianFeeds` | Asian countries | IN, JP, CN |

### 🔄 Combined Operations

| Command | Description | Usage |
|---------|-------------|-------|
| `validateAndDiscover` | Validate + cleanup + discover | `./gradlew :scripts:validateAndDiscover -Pcountries=US,GB` |
| `fullMaintenanceRss` | Complete maintenance workflow | `./gradlew :scripts:fullMaintenanceRss` |

### ⚡ Utility Commands

| Command | Description | Usage |
|---------|-------------|-------|
| `generateKotlinFeeds` | Generate Kotlin code files | `./gradlew :scripts:generateKotlinFeeds -Pcountries=US,GB` |
| `fastDiscovery` | Quick discovery for testing | `./gradlew :scripts:fastDiscovery -Pcountries=US,GB` |

## �📖 Command Categories

### 1. Validation & Quality Control

#### Validate Existing Feeds
```bash
# Basic validation
./gradlew :scripts:validateRssFeeds

# Custom parameters
./gradlew :scripts:validateRssFeeds -Pworkers=20 -Ptimeout=10 -Pdays=3

Parameters:

workers: Number of parallel workers (default: 10)
timeout: Request timeout in seconds (default: 15)
days: Days threshold for recent content (default: 7)
Cleanup Problematic Feeds

# Moderate cleanup (recommended)
./gradlew :scripts:cleanupRssFeeds -Premove=moderate

# Strict cleanup (removes outdated content)
./gradlew :scripts:cleanupRssFeeds -Premove=strict

# Conservative cleanup (only broken feeds)
./gradlew :scripts:cleanupRssFeeds -Premove=loose

# Cleanup Modes:

- strict: Removes inaccessible, invalid, OR outdated feeds
- moderate: Removes inaccessible OR invalid feeds (recommended)
- loose: Removes only completely inaccessible feeds
# 2. Feed Discovery
Discover for Specific Countries

# Single country
./gradlew :scripts:discoverRssFeeds -Pcountries=US

# Multiple countries
./gradlew :scripts:discoverRssFeeds -Pcountries=US,GB,CA,AU

# Specific categories
./gradlew :scripts:discoverRssFeeds -Pcountries=US,GB -Pcategories=news,sports

# News only
./gradlew :scripts:discoverRssFeeds -Pcountries=DE,FR -Pcategories=news

Auto-Add Discovered Feeds

# Discover and add to RssData.kt
./gradlew :scripts:discoverAndAddFeeds -Pcountries=US,GB,CA

# With specific categories
./gradlew :scripts:discoverAndAddFeeds -Pcountries=US,GB -Pcategories=news

Regional Discovery
# Major English-speaking countries (US, GB, CA, AU)
./gradlew :scripts:discoverMajorCountries

# European countries
./gradlew :scripts:discoverEuropeanFeeds

# Asian countries (IN, JP, CN)
./gradlew :scripts:discoverAsianFeeds

3. Combined Workflows
Validate and Discover

# Complete workflow: validate, cleanup, discover, add
./gradlew :scripts:validateAndDiscover -Pcountries=US,GB,CA -Premove=moderate

# Strict cleanup with discovery
./gradlew :scripts:validateAndDiscover -Pcountries=US,GB -Premove=strict

Full Maintenance
# Automated complete maintenance
./gradlew :scripts:fullMaintenanceRss

This command performs:

Validates existing feeds
Removes problematic feeds (moderate mode)
Discovers new feeds for major countries
Automatically adds them to RssData.kt

## 💡 Usage Examples

### Daily/Weekly Maintenance

```bash
# Weekly feed health check
./gradlew :scripts:validateRssFeeds

# Monthly cleanup and discovery
./gradlew :scripts:validateAndDiscover -Pcountries=US,GB,CA,AU -Premove=moderate

Adding New Countries

# Add feeds for new European markets
./gradlew :scripts:discoverAndAddFeeds -Pcountries=DE,FR,ES,IT

# Test feeds for new countries first
./gradlew :scripts:discoverRssFeeds -Pcountries=SE,NO,DK

Quality Control

# Strict cleanup before release
./gradlew :scripts:cleanupRssFeeds -Premove=strict

# Validate with custom parameters
./gradlew :scripts:validateRssFeeds -Pworkers=25 -Ptimeout=8 -Pdays=1

Development & Testing

# Quick test discovery
./gradlew :scripts:fastDiscovery -Pcountries=US,GB

# Generate code files without modifying main file
./gradlew :scripts:generateKotlinFeeds -Pcountries=US,GB,CA

## Adding a New Country

To add support for a new country, follow these steps:

1.  **Add the country to `RssData.kt`**:
    *   Open `app/src/main/java/secret/news/club/infrastructure/rss/RssData.kt`.
    *   Add a new function for the country, following the existing format (e.g., `getNewCountryRssServices()`).
    *   Add the new country to the `when` statement in the `getRssServicesByCountry` function.
    *   Add the country and its language to the `getLanguageForCountry` function.

2.  **Add the country to `validate_rss.py`**:
    *   Open `scripts/validate_rss.py`.
    *   Add the country's top news domains to the `country_domains` dictionary.
    *   Add the country's top YouTube news channels to the `youtube_channels` dictionary.
    *   Add the country's name in different languages to the `country_names` dictionary.

3.  **Run the discovery script**:
    ```bash
    ./gradlew :scripts:discoverAndAddFeeds -Pcountries=XX
    ```
    (Replace `XX` with the new country's two-letter code)

## Troubleshooting

### Common Issues

*   **"Could not find function for country XX"**: This error means that the script could not find a function for the specified country in the `RssData.kt` file. Make sure you have added a function for the country, as described in the "Adding a New Country" section.

*   **"No new feeds discovered"**: This error means that the script could not find any new RSS feeds for the specified country. This could be because the script has already discovered all the available feeds, or because the script is unable to access the websites it needs to discover new feeds.

*   **"Permission denied"**: This error means that the script does not have the necessary permissions to write to the `RssData.kt` file. Make sure that the file is not read-only and that you have the necessary permissions to modify it.

### Getting Help

If you are still having trouble, please open an issue on the project's GitHub page.

## Discovery Methods

The script uses the following methods to discover new RSS feeds:

*   **Domain Scan**: The script scans a list of known domains for common RSS feed paths (e.g., `/rss`, `/feed`).

*   **Search Engines**: The script uses search engines to find RSS feeds for the specified country and categories.

*   **YouTube**: The script discovers YouTube RSS feeds for news channels.

*   **HTML Link**: The script finds RSS feed links in the HTML of a webpage.

*   **Anchor Link**: The script finds RSS feed links in the anchor tags of a webpage.
