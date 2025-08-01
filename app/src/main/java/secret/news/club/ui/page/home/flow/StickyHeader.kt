package secret.news.club.ui.page.home.flow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import secret.news.club.ui.ext.surfaceColorAtElevation
import secret.news.club.ui.theme.palette.onDark

@Composable
fun StickyHeader(
    dateString: String,
    isShowFeedIcon: Boolean,
    articleListTonalElevation: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceColorAtElevation(articleListTonalElevation.dp)
                        onDark MaterialTheme.colorScheme.surface
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(
                    start = if (isShowFeedIcon) 54.dp else 24.dp,
                )
                .padding(top = 8.dp, bottom = 4.dp),
            text = dateString,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}