package secret.news.club.infrastructure.preference

import android.content.Context
import secret.news.club.R
import secret.news.club.ui.page.settings.accounts.AccountViewModel

sealed class SyncOnStartPreference(
    val value: Boolean,
) {

    object On : SyncOnStartPreference(true)
    object Off : SyncOnStartPreference(false)

    fun put(accountId: Int, viewModel: AccountViewModel) {
        viewModel.update(accountId) { copy(syncOnStart = this@SyncOnStartPreference) }
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

operator fun SyncOnStartPreference.not(): SyncOnStartPreference =
    when (value) {
        true -> SyncOnStartPreference.Off
        false -> SyncOnStartPreference.On
    }
