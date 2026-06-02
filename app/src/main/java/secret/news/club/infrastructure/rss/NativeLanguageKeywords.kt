package secret.news.club.infrastructure.rss

/**
 * Maps ISO country codes to their primary BCP-47 language code.
 *
 * Used by [FeedsPage] (via the `hasNativeScript` extension) to sort feeds whose
 * names are written in the user's native script to the top of the list. The
 * country code comes from the selected country preference; the returned
 * language code is matched against the per-feed `Feed.language` field as a
 * Latin-script fallback when Unicode-range script detection can't disambiguate.
 *
 * Defaults to `"en"` for unknown countries.
 */
object NativeLanguageKeywords {

    fun languageForCountry(countryCode: String): String =
        primaryLanguage[countryCode.uppercase()] ?: "en"

    private val primaryLanguage: Map<String, String> = mapOf(
        // English-speaking
        "US" to "en", "GB" to "en", "CA" to "en", "AU" to "en",
        "NZ" to "en", "IE" to "en", "ZA" to "en", "KE" to "en", "NG" to "en",
        "IN" to "en",
        // German
        "DE" to "de", "AT" to "de", "CH" to "de",
        // French
        "FR" to "fr", "BE" to "fr",
        // Spanish
        "ES" to "es", "MX" to "es", "AR" to "es", "CO" to "es", "PE" to "es",
        "VE" to "es", "CL" to "es", "EC" to "es", "GT" to "es", "CU" to "es",
        // Portuguese
        "BR" to "pt", "PT" to "pt",
        // Italian
        "IT" to "it",
        // Dutch
        "NL" to "nl",
        // Nordic
        "SE" to "sv", "NO" to "no", "DK" to "da", "FI" to "fi",
        // Slavic
        "PL" to "pl", "CZ" to "cs", "RU" to "ru", "UA" to "uk", "BG" to "bg",
        // East Asian
        "JP" to "ja", "CN" to "zh", "TW" to "zh", "HK" to "zh", "KR" to "ko",
        // Other
        "TR" to "tr", "GR" to "el", "HU" to "hu",
        // Arabic
        "SA" to "ar", "AE" to "ar", "EG" to "ar", "JO" to "ar", "LB" to "ar",
        "SY" to "ar", "IQ" to "ar", "KW" to "ar", "QA" to "ar", "BH" to "ar",
        "OM" to "ar", "YE" to "ar", "MA" to "ar", "TN" to "ar", "DZ" to "ar",
        "LY" to "ar", "SD" to "ar",
        // Hebrew, Persian
        "IL" to "he", "IR" to "fa",
        // South Asian
        "BD" to "bn", "PK" to "ur", "LK" to "si", "NP" to "ne",
        // South-East Asian
        "TH" to "th", "VN" to "vi", "ID" to "id", "MY" to "ms", "PH" to "tl",
    )
}
