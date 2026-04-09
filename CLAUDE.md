# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Secret News Club is an Android RSS reader app in Material You style. It supports multiple account types (Local, Fever, Google Reader, FreshRSS) and features OPML import/export, background sync, full-content parsing, and read-aloud.

- **Application ID:** `secret.news.club`
- **Min SDK:** 26, **Target/Compile SDK:** 36
- **Version:** 0.14.10 (code 43)
- **Kotlin:** 2.2.0, **Java target:** 11
- **Build Flavors:** `github` (default), `fdroid`, `googlePlay` (suffix `.google.play`)

## Common Commands

```bash
# Build
./gradlew :app:assembleDebug
./gradlew :app:assembleRelease          # Requires signature/keystore.properties
./gradlew :app:assembleGithubRelease    # Build specific flavor

# Test & Lint
./gradlew test                          # All unit tests
./gradlew :app:testDebugUnitTest --tests "secret.news.club.SomeTest.methodName"  # Single test
./gradlew :app:connectedAndroidTest     # Instrumented tests (requires device/emulator)
./gradlew :app:lint                     # Lint (MissingTranslation/ExtraTranslation suppressed)

# RSS Scripts
./gradlew :scripts:validateRssFeeds
./gradlew :scripts:discoverRssFeeds -Pcountries=US,GB
./gradlew :scripts:cleanupRssFeeds
./gradlew :scripts:discoverAndAddFeeds
```

## Architecture

**Clean Architecture + MVVM** with a single-activity pattern and Jetpack Compose navigation.

### Layer Structure

```
secret.news.club/
├── domain/          # Business logic: models, repository interfaces, services
├── infrastructure/  # Implementations: Room DB, Retrofit/OkHttp, Hilt DI, DataStore, RSS parsing
└── ui/              # Jetpack Compose screens, components, Material 3 theming
```

### Key Architectural Patterns

- **Reactive state:** Kotlin Flow / StateFlow throughout — DAOs return `Flow<T>`, ViewModels expose `StateFlow`
- **DI:** Hilt with modules in `infrastructure/di/` (OkHttp, Retrofit, Database, UseCase, Worker, ImageLoader, CoroutineScope, etc.)
- **Multi-account:** `AccountService` manages the active account; `RssService` is implemented per account type (`LocalRssService`, `FeverRssService`, `GoogleReaderRssService`, `FreshRssService`)
- **Background sync:** WorkManager-based `SyncWorker`
- **Database:** Room v7 with auto-migrations; entities: `Account`, `Feed`, `Article`, `Group`, `ArchivedArticle`; schemas tracked in `app/schemas/`
- **Pagination:** Paging 3 used for article lists
- **Theming:** Custom Monet dynamic color implementation in `ui/theme/palette/` (supports CIELAB, OKLab, ZCAM color spaces)

### RSS Provider API

Network providers live in `infrastructure/rss/provider/`:
- `fever/` — Fever API (FeverAPI.kt + FeverDTO.kt)
- `greader/` — Google Reader compatible API (used by both GoogleReader and FreshRSS)

`RssData.kt` in `infrastructure/rss/` contains curated predefined RSS feed lists organized by country.

### Key Technology Stack

| Concern | Library |
|---------|---------|
| UI | Jetpack Compose + Material 3 |
| DI | Hilt / Dagger |
| DB | Room (SQLite) |
| Network | Retrofit 2 + OkHttp 5 alpha |
| Preferences | DataStore (encrypted) |
| Images | Coil (SVG + GIF support) |
| RSS Parsing | Rome + Readability4J |
| Background | WorkManager |
| Pagination | Paging 3 |
| Logging | Timber |

## Important Files

- `app/build.gradle.kts` — flavors, signing, Compose feature flags, APK naming (includes git hash)
- `gradle/libs.versions.toml` — version catalog for all dependencies
- `app/proguard-rules.pro` — ProGuard rules for release
- `infrastructure/db/AndroidDatabase.kt` — Room database setup and migration definitions
- `infrastructure/di/` — all Hilt module definitions
- `scripts/validate_rss.py` — Python tool for RSS feed validation/discovery (managed by Gradle tasks)
- `fastlane/` — release management configuration
- `signature/keystore.properties` — signing config (not in repo; required for release builds)