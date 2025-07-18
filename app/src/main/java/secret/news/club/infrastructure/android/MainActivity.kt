package secret.news.club.infrastructure.android

import android.Manifest
import android.content.Intent
import android.database.CursorWindow
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.DisposableEffect
import androidx.core.app.NotificationManagerCompat
import androidx.core.util.Consumer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.profileinstaller.ProfileInstallerInitializer
import coil.ImageLoader
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field
import javax.inject.Inject
import secret.news.club.domain.service.AccountService
import secret.news.club.domain.service.DefaultSubscriptionManager
import secret.news.club.infrastructure.compose.ProvideCompositionLocals
import secret.news.club.infrastructure.preference.AccountSettingsProvider
import secret.news.club.infrastructure.preference.LanguagesPreference
import secret.news.club.infrastructure.preference.SettingsProvider
import secret.news.club.ui.ext.languages
import secret.news.club.ui.page.common.ExtraName
import secret.news.club.ui.page.common.HomeEntry
import secret.news.club.ui.page.common.RouteName
import secret.news.club.ui.page.home.feeds.subscribe.SubscribeViewModel

/** The Single-Activity Architecture. */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var imageLoader: ImageLoader

    @Inject lateinit var settingsProvider: SettingsProvider

    @Inject lateinit var accountService: AccountService

    @Inject lateinit var defaultSubscriptionManager: DefaultSubscriptionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        Log.i("RLog", "onCreate: ${ProfileInstallerInitializer().create(this)}")

        defaultSubscriptionManager.addDefaultSubscriptions()
        enableEdgeToEdge()

        // Set the language
        if (Build.VERSION.SDK_INT < 33) {
            LanguagesPreference.fromValue(languages).let { LanguagesPreference.setLocale(it) }
        }

        // Workaround for https://github.com/Ashinch/ReadYou/issues/312: increase cursor window size
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024) // 100MB is the new cursor window size
        } catch (e: Exception) {
            Log.e("RLog", "Unable to increase cursor window size: ${e.printStackTrace()}")
        }

        val requestPermissionLauncher =
            this.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted
                ->
                if (isGranted) {} else { // Permission denied }
                }
            }

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                !NotificationManagerCompat.from(this).areNotificationsEnabled()
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            AccountSettingsProvider(accountService = accountService) {
                settingsProvider.ProvidesSettings {
                    val subscribeViewModel: SubscribeViewModel = hiltViewModel()
                    val navController = rememberNavController()

                    ProvideCompositionLocals {
                        HomeEntry(
                            subscribeViewModel = subscribeViewModel,
                            navController = navController,
                        )
                    }

                    DisposableEffect(this) {
                        val listener =
                            Consumer<Intent> { intent ->
                                intent.getLaunchAction()?.let { action ->
                                    when (action) {
                                        is LaunchAction.OpenArticle -> {
                                            navController.navigate(
                                                "${RouteName.READING}/${action.articleId}"
                                            ) {
                                                launchSingleTop = true
                                            }
                                        }

                                        is LaunchAction.Subscribe -> {
                                            subscribeViewModel.handleSharedUrlFromIntent(action.url)
                                            val res =
                                                navController.popBackStack(
                                                    route = RouteName.FEEDS,
                                                    inclusive = false,
                                                    saveState = true,
                                                )
                                            if (!res)
                                                navController.navigate(route = RouteName.FEEDS)
                                        }
                                    }
                                }
                            }
                        listener.accept(intent) // consume the launch intent as well
                        addOnNewIntentListener(listener)
                        onDispose { removeOnNewIntentListener(listener) }
                    }
                }
            }
        }
    }
}

sealed interface LaunchAction {
    data class Subscribe(val url: String) : LaunchAction

    data class OpenArticle(val articleId: String) : LaunchAction
}

private fun Intent.getLaunchAction(): LaunchAction? {
    return when (action) {
        Intent.ACTION_VIEW -> {
            dataString?.let { LaunchAction.Subscribe(it) }
        }

        Intent.ACTION_SEND -> {
            getStringExtra(Intent.EXTRA_TEXT)
                ?.also { removeExtra(Intent.EXTRA_TEXT) }
                ?.let { LaunchAction.Subscribe(it) }
        }

        else -> {
            getStringExtra(ExtraName.ARTICLE_ID)
                ?.also { removeExtra(ExtraName.ARTICLE_ID) }
                ?.let { LaunchAction.OpenArticle(it) }
        }
    }
}
