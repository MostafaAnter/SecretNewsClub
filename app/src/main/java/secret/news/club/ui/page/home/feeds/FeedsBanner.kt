package secret.news.club.ui.page.home.feeds

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import secret.news.club.domain.model.general.Filter
import secret.news.club.ui.component.base.Banner

@Composable
fun FeedsBanner(
    modifier: Modifier = Modifier,
    filter: Filter,
    desc: String? = null,
    onClick: () -> Unit = {},
) {
    Banner(
        modifier = modifier,
        title = filter.toName(),
        desc = desc,
        icon = filter.iconOutline,
        action = {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
            )
        },
        onClick = onClick
    )
}