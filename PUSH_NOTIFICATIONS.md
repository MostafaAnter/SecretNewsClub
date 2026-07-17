# Sending Push Notifications

Maintainer guide for broadcasting a push notification to users via Firebase Cloud
Messaging. This is a manual, Firebase Console–driven workflow — there's no backend or
script involved for v1.

Applies to the `github` and `googlePlay` build flavors only. The `fdroid` flavor ships
no Firebase code at all (see [CLAUDE.md](CLAUDE.md#firebase-setup-push--analytics)),
per F-Droid's anti-tracking inclusion policy.

## How it works

- Every install with the **Push notifications** setting on (Settings → Interaction)
  subscribes to the FCM topic `broadcast_all`, plus a per-country topic
  `broadcast_country_<CODE>` (e.g. `broadcast_country_US`) matching the device's
  selected country. Changing country re-subscribes automatically.
- Messages are handled as **data-only** payloads by `PushMessagingService`, which
  builds the notification itself (title/body/image, tap-to-open) — this is what makes
  it work consistently whether the app is foregrounded, backgrounded, or killed.
- Tapping the notification opens the article directly: if it's already synced locally
  it opens instantly, otherwise the app fetches and renders it on demand.

## Prerequisites

1. A real `google-services.json` in `app/` (the committed one is a placeholder —
   swap in the real file from the Firebase Console project; see CLAUDE.md).
2. A debug or release build installed on a test device, built **after** the real
   `google-services.json` is in place.
3. In the app: Settings → Interaction → **Push notifications** toggled on, and
   notification permission granted.
4. Optional but recommended: open Settings → Interaction → **Troubleshooting** →
   "Push test helper (debug)" (debug builds only) to copy a real, already-synced
   article's title/URL/image straight from the device — this is the fastest way to
   get valid test values.

## Sending from the Firebase Console

1. Go to the [Firebase Console](https://console.firebase.google.com/) → your project
   → **Engage → Messaging**.
2. **New campaign → Notifications**.
3. Fill in **Notification title** / **Notification text** — required by the composer,
   but the app doesn't use these directly (see [Caveat](#caveat-notification-vs-data-only)
   below). Anything non-empty works, e.g. "Test".
4. **Next**.
5. **Target** → choose **Topic**, then select/type:
   - `broadcast_all` — every subscribed device, or
   - `broadcast_country_US` (or whichever code) — only devices with that country selected.
6. **Next** through Scheduling (leave as "Now" for an immediate test).
7. Under **Additional options → Custom data**, add these key/value rows:

   | key | required | value |
   |---|---|---|
   | `title` | yes | the notification title shown to users |
   | `body` | no | short body text |
   | `article_url` | yes | the article's canonical URL |
   | `image_url` | no | an image URL for the notification |

8. **Review** → **Publish now**.

## Verifying it worked

- The notification should appear within a few seconds, styled via
  `NotificationHelper.notifyPush` (channel: "Announcements").
- Tapping it should open the article directly.
- With `adb shell setprop debug.firebase.analytics.app secret.news.club` running,
  Firebase Console → Analytics → **DebugView** should show `push_received` then
  `push_opened` events.
- Firebase Console → Engage → Messaging → **Audience** tab shows which topics have
  subscribed devices (subscription may take a couple of minutes to appear after
  enabling the setting).

## Caveat: notification vs. data-only

The Console composer **always** attaches a notification payload (title/text are
mandatory fields) in addition to the custom data. This matters because Android's FCM
SDK intercepts messages that carry a notification payload *before* app code runs,
whenever the app is backgrounded or killed:

- **App in foreground**: `PushMessagingService.onMessageReceived` still fires
  normally — no issue.
- **App backgrounded or killed**: Android auto-displays a plain system notification
  using the Console's title/text and **skips `PushMessagingService` entirely** —
  you won't see the styled notification, and tapping it just opens the app instead
  of the article.

So Console-composed sends are fine for foreground testing, but don't fully exercise
the killed-app path the app is actually designed for. Verifying that path requires
sending a true data-only message (no `notification` key at all) via the
[FCM HTTP v1 API](https://firebase.google.com/docs/cloud-messaging/send-message) with
a service-account key, rather than the Console UI.
