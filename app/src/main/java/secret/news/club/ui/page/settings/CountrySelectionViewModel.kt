package secret.news.club.ui.page.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import secret.news.club.domain.service.DefaultSubscriptionManager
import javax.inject.Inject

@HiltViewModel
class CountrySelectionViewModel @Inject constructor(
    val subscriptionManager: DefaultSubscriptionManager
) : ViewModel()
