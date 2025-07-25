package secret.news.club.infrastructure.preference

import android.content.Context
import secret.news.club.R
import secret.news.club.ui.page.settings.accounts.AccountViewModel

sealed class SyncOnlyOnWiFiPreference(
    val value: Boolean,
) {

    object On : SyncOnlyOnWiFiPreference(true)
    object Off : SyncOnlyOnWiFiPreference(false)

    fun put(accountId: Int, viewModel: AccountViewModel) {
        viewModel.update(accountId) { copy(syncOnlyOnWiFi = this@SyncOnlyOnWiFiPreference) }
    }

    fun toDesc(context: Context): String =
        when (this) {
            On -> context.getString(R.string.on)
            Off -> context.getString(R.string.off)
        }

    companion object {

        val default = Off
        val values = listOf(On, Off)
    }
}

operator fun SyncOnlyOnWiFiPreference.not(): SyncOnlyOnWiFiPreference =
    when (value) {
        true -> SyncOnlyOnWiFiPreference.Off
        false -> SyncOnlyOnWiFiPreference.On
    }
