package proyecto.com.hackersnew.features.news.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import proyecto.com.hackersnew.R
import proyecto.com.hackersnew.ui.theme.HackersNewTheme

@Composable
fun PaginationLoadingItem(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .semantics {
                contentDescription = "Cargando más noticias"
            },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            strokeWidth = 3.dp
        )
    }
}

@Composable
fun PaginationErrorItem(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
        TextButton(onClick = onRetry) {
            Text(text = stringResource(R.string.news_button_retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PaginationLoadingItemPreview() {
    HackersNewTheme(dynamicColor = false) {
        PaginationLoadingItem(modifier = Modifier.fillMaxWidth())
    }
}

@Preview(showBackground = true)
@Composable
private fun PaginationErrorItemPreview() {
    HackersNewTheme(dynamicColor = false) {
        PaginationErrorItem(
            message = "Error al cargar más noticias",
            onRetry = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
