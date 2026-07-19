package secret.news.club.infrastructure.firebase

import secret.news.club.domain.service.CrashReportingService
import javax.inject.Inject

/**
 * No-op binding for the fdroid flavor, which ships no Firebase/Play Services
 * dependency at all per F-Droid's anti-tracking inclusion policy.
 */
class NoOpCrashReportingService @Inject constructor() : CrashReportingService {
    override fun recordException(throwable: Throwable) = Unit
    override fun log(message: String) = Unit
    override fun setCustomKey(key: String, value: String) = Unit
}
