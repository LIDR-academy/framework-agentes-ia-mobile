package proyecto.com.hackersnew.features.news.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import proyecto.com.hackersnew.features.news.presentation.model.StoryUiModel
import proyecto.com.hackersnew.ui.theme.HackerNewsOrange
import proyecto.com.hackersnew.ui.theme.HackersNewTheme

@Composable
fun NewsItemCard(
    story: StoryUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val a11yDescription = "${story.position}. ${story.title}, " +
        "${story.score} puntos por ${story.author}, " +
        "${story.commentCount} comentarios, ${story.domain}, ${story.relativeTime}"

    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = a11yDescription
            },
        verticalAlignment = Alignment.Top
    ) {
        // Número de posición
        Text(
            text = "${story.position}.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Título
            Text(
                text = story.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Línea 2: Score + autor
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "▲",
                    color = HackerNewsOrange,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = " ${story.score} pts",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                DotSeparator()
                Text(
                    text = "by ${story.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Línea 3: Comentarios + dominio + tiempo
            Row(
                modifier = Modifier.padding(top = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "\uD83D\uDCAC ${story.commentCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                DotSeparator()
                Text(
                    text = story.domain,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                DotSeparator()
                Text(
                    text = story.relativeTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DotSeparator() {
    Text(
        text = " · ",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview(showBackground = true)
@Composable
private fun NewsItemCardPreview() {
    HackersNewTheme(dynamicColor = false) {
        NewsItemCard(
            story = StoryUiModel(
                id = 1,
                position = 1,
                title = "Show HN: I built a tool to generate native mobile apps with AI",
                score = 245,
                author = "dhouston",
                commentCount = 71,
                domain = "github.com",
                relativeTime = "hace 2h",
                url = "https://github.com/example/project"
            ),
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsItemCardLongTitlePreview() {
    HackersNewTheme(dynamicColor = false) {
        NewsItemCard(
            story = StoryUiModel(
                id = 2,
                position = 42,
                title = "A very long title that should wrap to multiple lines and eventually get truncated with an ellipsis if it exceeds three lines of text in the card component",
                score = 1024,
                author = "longusername",
                commentCount = 512,
                domain = "blog.some-very-long-domain.co.uk",
                relativeTime = "hace 3d",
                url = "https://blog.some-very-long-domain.co.uk/article"
            ),
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
