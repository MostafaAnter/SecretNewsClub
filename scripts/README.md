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

## 📚 Available Commands

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

## 📖 Command Categories

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

