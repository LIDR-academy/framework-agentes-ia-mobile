package proyecto.com.hackersnew.features.news.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import proyecto.com.hackersnew.ui.theme.HackersNewTheme

private const val SHIMMER_CARD_COUNT = 5

@Composable
fun NewsShimmerList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.semantics {
            contentDescription = "Cargando noticias"
        }
    ) {
        repeat(SHIMMER_CARD_COUNT) { index ->
            NewsShimmerCard(
                modifier = Modifier.fillMaxWidth()
            )
            if (index < SHIMMER_CARD_COUNT - 1) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
private fun NewsShimmerCard(
    modifier: Modifier = Modifier
) {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.surfaceVariant
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim.value - 200f, 0f),
        end = Offset(translateAnim.value, 0f)
    )

    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Placeholder número de posición
        ShimmerBox(
            brush = brush,
            modifier = Modifier
                .width(24.dp)
                .height(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Placeholder título (2 líneas)
            ShimmerBox(
                brush = brush,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerBox(
                brush = brush,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(18.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Placeholder línea 2 (score + autor)
            ShimmerBox(
                brush = brush,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(14.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Placeholder línea 3 (comentarios + dominio + tiempo)
            ShimmerBox(
                brush = brush,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(12.dp)
            )
        }
    }
}

@Composable
private fun ShimmerBox(
    brush: Brush,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(brush)
    )
}

@Preview(showBackground = true)
@Composable
private fun NewsShimmerListPreview() {
    HackersNewTheme(dynamicColor = false) {
        NewsShimmerList(
            modifier = Modifier.fillMaxSize()
        )
    }
}
