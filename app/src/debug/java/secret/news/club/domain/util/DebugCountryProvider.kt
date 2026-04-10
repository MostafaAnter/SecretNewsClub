package secret.news.club.domain.util

/**
 * Provides a country code for debugging purposes.
 *
 * To test a different country, change the value of [TEST_COUNTRY_CODE].
 * For example, to test for Germany, set it to "DE".
 *
 * If you want to use the real country detection logic, set this to `null`.
 */
val TEST_COUNTRY_CODE: String? = null // Set to a 2-letter code (e.g. "DE") to override country detection.
fun getDebugCountryCode(): String? {
    return TEST_COUNTRY_CODE
}
