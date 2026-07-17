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

## Firebase setup (push + analytics)

Firebase (Cloud Messaging + Analytics) is wired for the `github` and `googlePlay` flavors only — `fdroid` ships neither dependency nor code that references `com.google.firebase.*`, per F-Droid's anti-tracking/non-free-network inclusion policy. Common code (`MainActivity`, `AndroidApp`, `NotificationHelper`) only ever depends on the `PushAnalyticsService` interface (`domain/service/PushAnalyticsService.kt`); each flavor binds its own implementation (`FirebasePushAnalyticsService` for github/googlePlay, `NoOpPushAnalyticsService` for fdroid).

- `app/google-services.json` is committed as a **non-functional placeholder** (mirrors `signature/keystore.properties`'s pattern). Replace it locally with the real file from the Firebase Console project before testing push/analytics or building a release you intend to actually distribute. The Firebase project should register **two** Android apps — `secret.news.club` (covers `github`) and `secret.news.club.google.play` (covers `googlePlay`, due to `applicationIdSuffix`) — both land in one downloaded `google-services.json`.
- Every install subscribes to the FCM topic `broadcast_all` **and** a per-country topic `broadcast_country_<COUNTRY_CODE>` (e.g. `broadcast_country_US`) matching the user's selected country setting, when the "Push notifications" setting is on (Settings → Interaction → Feeds page, hidden on fdroid). Changing the selected country unsubscribes from the old country topic and subscribes to the new one — only one country topic is held at a time (tracked via `pushSubscribedCountryTopic` in DataStore). Send a broadcast from Firebase Console → Cloud Messaging → New notification/campaign, targeting either `broadcast_all` (everyone) or a specific `broadcast_country_<CODE>` topic, and set the **data payload** (not the notification-message fields) with these keys:

  | key | required | meaning |
  |---|---|---|
  | `title` | yes | notification title |
  | `body` | no | notification body |
  | `article_url` | yes | canonical article URL — used to find/open the article on tap |
  | `image_url` | no | optional large-icon image |

  Messages are handled entirely by `PushMessagingService` (data-only, not an FCM "notification" message), so the same behavior applies whether the app is foregrounded, backgrounded, or killed.
- DAU needs no custom code — Firebase Analytics' automatic `session_start`/`first_open` events populate the standard "Active Users" dashboard once the SDK is linked to a real project.

## Important Files

- `app/build.gradle.kts` — flavors, signing, Compose feature flags, APK naming (includes git hash)
- `gradle/libs.versions.toml` — version catalog for all dependencies
- `app/proguard-rules.pro` — ProGuard rules for release
- `infrastructure/db/AndroidDatabase.kt` — Room database setup and migration definitions
- `infrastructure/di/` — all Hilt module definitions
- `scripts/validate_rss.py` — Python tool for RSS feed validation/discovery (managed by Gradle tasks)
- `fastlane/` — release management configuration
- `signature/keystore.properties` — signing config (not in repo; required for release builds)