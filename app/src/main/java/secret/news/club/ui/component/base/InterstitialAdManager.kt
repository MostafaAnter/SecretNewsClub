package secret.news.club.ui.component.base

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-2111511075794057/8504265452"
private const val SHOW_EVERY_N_OPENS = 3

class InterstitialAdManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var openCount = 0

    fun loadAd() {
        if (isLoading || interstitialAd != null) return
        isLoading = true
        InterstitialAd.load(
            context,
            INTERSTITIAL_AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                }
            }
        )
    }

    fun showIfReady(activity: Activity, onComplete: () -> Unit) {
        openCount++
        val ad = interstitialAd
        if (openCount % SHOW_EVERY_N_OPENS == 0 && ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadAd()
                    onComplete()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    interstitialAd = null
                    loadAd()
                    onComplete()
                }
            }
            ad.show(activity)
        } else {
            if (interstitialAd == null && !isLoading) loadAd()
            onComplete()
        }
    }
}