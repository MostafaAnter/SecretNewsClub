package secret.news.club.ui.page.settings.contact

import android.content.Intent
import android.net.Uri
import android.view.HapticFeedbackConstants
import android.view.SoundEffectConstants
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.DropShadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Morph
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import secret.news.club.R
import secret.news.club.infrastructure.preference.OpenLinkPreference
import secret.news.club.ui.component.base.FeedbackIconButton
import secret.news.club.ui.component.base.RYScaffold
import secret.news.club.ui.ext.getCurrentVersion
import secret.news.club.ui.ext.openURL
import secret.news.club.ui.ext.showToast
import secret.news.club.ui.graphics.MorphPolygonShape
import secret.news.club.ui.theme.palette.alwaysLight
import secret.news.club.ui.theme.palette.onLight


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private val ShapeGacha by lazy {
    buildList {
        MaterialShapes.run {
            add(Cookie12Sided)
            add(Cookie4Sided)
            add(Cookie6Sided)
            add(Cookie7Sided)
            add(Cookie9Sided)
            add(Clover8Leaf)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ContactUsPage(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    var currentVersion by remember { mutableStateOf("") }

    val morphProgress = remember { Animatable(0f) }

    val polygonShape = remember { ShapeGacha.random() }
    val circle = MaterialShapes.Circle
    val morph = Morph(polygonShape, circle)

    val shadowShape by remember {
        derivedStateOf {
            MorphPolygonShape(morph, morphProgress.value)
        }
    }

    val bgShape by remember {
        derivedStateOf {
            MorphPolygonShape(morph, morphProgress.value)
        }
    }

    val morphSpec = MaterialTheme.motionScheme.slowEffectsSpec<Float>()
    val colorScheme = MaterialTheme.colorScheme

    val colorGacha = remember {
        listOf(
            colorScheme.primaryFixed,
            colorScheme.secondaryFixed,
            colorScheme.tertiaryFixed
        )
    }

    val logoBGColor = remember { colorGacha.random() }


    LaunchedEffect(Unit) {
        currentVersion = context.getCurrentVersion().toString()
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Column(
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                    scope.launch { morphProgress.animateTo(1f, morphSpec) }
                                    tryAwaitRelease()
                                    scope.launch { morphProgress.animateTo(0f, morphSpec) }
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                    view.playSoundEffect(SoundEffectConstants.CLICK)
                                },
                                onTap = {
                                    context.showToast(context.getString(R.string.is_latest_version))
                                }
                            )
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .background(color = logoBGColor, shape = bgShape)
                                .dropShadow(
                                    shape = shadowShape,
                                    DropShadow(
                                        radius = 24.dp,
                                        spread = 16.dp,
                                        alpha = .1f,
                                        color = logoBGColor
                                    )
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(
                                modifier = Modifier.size(90.dp),
                                painter = painterResource(R.mipmap.ic_launcher_round),
                                contentDescription = stringResource(R.string.read_you),
                            )
                        }
                        Spacer(modifier = Modifier.height(48.dp))
                        BadgedBox(
                            badge = {
                                Badge(
                                    modifier = Modifier.animateContentSize(tween(800)),
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    contentColor = MaterialTheme.colorScheme.tertiary,
                                ) {
                                    Text(text = currentVersion)
                                }
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.contact_us),
                                style = MaterialTheme.typography.displaySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Contact
                        RoundIconButton(
                            RoundIconButtonType.Contact(
                                backgroundColor = MaterialTheme.colorScheme.primaryContainer alwaysLight true,
                            ) {
                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                context.openURL(
                                    "mailto:" + context.getString(R.string.email_address),
                                    OpenLinkPreference.AutoPreferCustomTabs
                                )
                            })

                        Spacer(modifier = Modifier.width(16.dp))

                        // Telegram
                        RoundIconButton(
                            RoundIconButtonType.Telegram(
                                backgroundColor = MaterialTheme.colorScheme.primaryContainer alwaysLight true,
                            ) {
                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                context.openURL(
                                    context.getString(R.string.telegram_link),
                                    OpenLinkPreference.AutoPreferCustomTabs
                                )
                            })


                    }
                }
            }
        }
    )
}

@Immutable
sealed class RoundIconButtonType(
    val iconResource: Int? = null,
    val iconVector: ImageVector? = null,
    val descResource: Int? = null,
    val descString: String? = null,
    open val size: Dp = 24.dp,
    open val offset: Modifier = Modifier.offset(),
    open val backgroundColor: Color = Color.Unspecified,
    open val onClick: () -> Unit = {},
) {

    @Immutable
    data class Telegram(
        val desc: String = "Telegram",
        override val offset: Modifier = Modifier.offset(x = (-1).dp),
        override val backgroundColor: Color,
        override val onClick: () -> Unit = {},
    ) : RoundIconButtonType(
        iconResource = R.drawable.ic_telegram,
        descString = desc,
        backgroundColor = backgroundColor,
        onClick = onClick,
    )

    @Immutable
    data class Contact(
        val desc: Int = R.string.contact_us,
        override val backgroundColor: Color,
        override val onClick: () -> Unit = {},
    ) : RoundIconButtonType(
        iconVector = Icons.Rounded.Email,
        descResource = desc,
        backgroundColor = backgroundColor,
        onClick = onClick,
    )
}

@Composable
private fun RoundIconButton(type: RoundIconButtonType) {
    IconButton(
        modifier = Modifier
            .size(70.dp)
            .background(
                color = type.backgroundColor,
                shape = CircleShape,
            ),
        onClick = { type.onClick() }
    ) {
        when (type) {
            is RoundIconButtonType.Contact -> {
                Icon(
                    modifier = type.offset.size(type.size),
                    imageVector = type.iconVector!!,
                    contentDescription = stringResource(type.descResource!!),
                    tint = MaterialTheme.colorScheme.onSurface alwaysLight true,
                )
            }

            is RoundIconButtonType.Telegram -> {
                Icon(
                    modifier = type.offset.size(type.size),
                    painter = painterResource(type.iconResource!!),
                    contentDescription = type.descString,
                    tint = MaterialTheme.colorScheme.onSurface alwaysLight true,
                )
            }
        }
    }
}
