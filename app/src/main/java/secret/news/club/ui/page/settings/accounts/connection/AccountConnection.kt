package secret.news.club.ui.page.settings.accounts.connection

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import secret.news.club.R
import secret.news.club.domain.model.account.Account
import secret.news.club.domain.model.account.AccountType
import secret.news.club.ui.component.base.Subtitle

@Composable
fun LazyItemScope.AccountConnection(
    account: Account,
) {
    if (account.type.id != AccountType.Local.id) {
        Subtitle(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(R.string.connection),
        )
    }
    when (account.type.id) {
        AccountType.Fever.id -> FeverConnection(account)
        AccountType.GoogleReader.id -> GoogleReaderConnection(account)
        AccountType.FreshRSS.id -> FreshRSSConnection(account)
        AccountType.Feedly.id -> {}
        AccountType.Inoreader.id -> {}
    }
    if (account.type.id != AccountType.Local.id) {
        Spacer(modifier = Modifier.height(24.dp))
    }
}
