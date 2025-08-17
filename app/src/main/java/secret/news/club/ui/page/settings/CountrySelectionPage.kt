package secret.news.club.ui.page.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import secret.news.club.R
import secret.news.club.infrastructure.preference.CountryPreference
import secret.news.club.ui.component.base.FeedbackIconButton
import secret.news.club.ui.component.base.RYScaffold
import secret.news.club.ui.theme.palette.onLight
import java.util.Locale

@Composable
fun CountrySelectionPage(
    navController: NavHostController,
    viewModel: CountrySelectionViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    val allCountries = remember {
        Locale.getISOCountries().map {
            val locale = Locale("", it)
            locale.displayCountry to it
        }.sortedBy { it.first }
    }

    val filteredCountries = remember(searchQuery, allCountries) {
        if (searchQuery.isEmpty()) {
            allCountries
        } else {
            allCountries.filter { (name, code) ->
                name.contains(searchQuery, ignoreCase = true) ||
                        code.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    RYScaffold(
        containerColor = MaterialTheme.colorScheme.surface onLight MaterialTheme.colorScheme.inverseOnSurface,
        navigationIcon = {
            FeedbackIconButton(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onSurface
            ) {
                navController.popBackStack()
            }
        },
        content = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(stringResource(R.string.search_country)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                LazyColumn {
                    items(filteredCountries) { (countryName, countryCode) ->
                        SelectableSettingItem(
                            title = countryName,
                            desc = countryCode,
                            onClick = {
                                scope.launch {
                                    CountryPreference.Country(countryCode).put(context, scope)
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}
