package secret.news.club.ui.page.home.reading

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import secret.news.club.R
import secret.news.club.infrastructure.preference.LocalReadingPageTonalElevation
import secret.news.club.infrastructure.preference.LocalSharedContent
import secret.news.club.infrastructure.preference.ReadingPageTonalElevationPreference
import secret.news.club.ui.component.base.FeedbackIconButton
import secret.news.club.ui.page.common.RouteName

private val sizeSpec = spring<IntSize>(stiffness = 700f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    isShow: Boolean,
    isScrolled: Boolean = false,
    title: String? = "",
    link: String? = "",
    onClick: (() -> Unit)? = null,
    onClose: () -> Unit = {},
) {
    val context = LocalContext.current
    val sharedContent = LocalSharedContent.current
    val isOutlined =
        LocalReadingPageTonalElevation.current == ReadingPageTonalElevationPreference.Outlined

    val containerColor by animateColorAsState(with(MaterialTheme.colorScheme) {
        if (isOutlined || !isScrolled) surface else surfaceContainer
    }, label = "", animationSpec = spring(stiffness = Spring.StiffnessMediumLow))


    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.drawBehind { drawRect(containerColor) }
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding()
                    ),
            )
            AnimatedVisibility(
                visible = isShow,
                enter = expandVertically(
                    expandFrom = Alignment.Bottom,
                    animationSpec = sizeSpec
                ),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Bottom,
                    animationSpec = sizeSpec
                )
            ) {
                TopAppBar(
                    title = {},
                    modifier = if (onClick == null) Modifier else Modifier.clickable(
                        onClick = onClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                    windowInsets = WindowInsets(0.dp),
                    navigationIcon = {
                        FeedbackIconButton(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.close),
                            tint = MaterialTheme.colorScheme.onSurface
                        ) {
                            onClose()
                        }
                    },
                    actions = {
                        FeedbackIconButton(
                            modifier = Modifier.size(22.dp),
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = stringResource(R.string.style),
                            tint = MaterialTheme.colorScheme.onSurface
                        ) {
                            navController.navigate(RouteName.READING_PAGE_STYLE) {
                                launchSingleTop = true
                            }
                        }
                        FeedbackIconButton(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Outlined.Share,
                            contentDescription = stringResource(R.string.share),
                            tint = MaterialTheme.colorScheme.onSurface,
                        ) {
                            sharedContent.share(context, title, link)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
            if (isOutlined && isScrolled) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    thickness = 0.5f.dp
                )
            }
        }
    }
}