package secret.news.club.infrastructure.firebase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import secret.news.club.domain.service.CrashReportingService
import javax.inject.Inject

class FirebaseCrashReportingService @Inject constructor() : CrashReportingService {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun recordException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun log(message: String) {
        crashlytics.log(message)
    }

    override fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }
}
