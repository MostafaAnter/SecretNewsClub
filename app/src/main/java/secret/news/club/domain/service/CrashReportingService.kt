package secret.news.club.domain.service

/**
 * Abstracts crash/non-fatal error reporting so shared code (CrashHandler, AndroidApp) never
 * references Firebase directly. The fdroid flavor binds a no-op implementation, since
 * F-Droid's inclusion policy forbids proprietary/tracking libraries.
 */
interface CrashReportingService {
    fun recordException(throwable: Throwable)
    fun log(message: String)
    fun setCustomKey(key: String, value: String)
}
