package proyecto.com.hackersnew.features.news.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import proyecto.com.hackersnew.R
import proyecto.com.hackersnew.features.news.presentation.components.NewsEmptyContent
import proyecto.com.hackersnew.features.news.presentation.components.NewsErrorContent
import proyecto.com.hackersnew.features.news.presentation.components.NewsItemCard
import proyecto.com.hackersnew.features.news.presentation.components.NewsShimmerList
import proyecto.com.hackersnew.features.news.presentation.components.PaginationErrorItem
import proyecto.com.hackersnew.features.news.presentation.components.PaginationLoadingItem
import proyecto.com.hackersnew.features.news.presentation.model.StoryUiModel
import proyecto.com.hackersnew.ui.theme.HackersNewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    uiState: NewsUiState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onStoryClick: (StoryUiModel) -> Unit,
    onRetry: () -> Unit,
    snackbarMessage: String? = null,
    onSnackbarDismissed: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val retryLabel = stringResource(R.string.news_button_retry)

    LaunchedEffect(snackbarMessage) {
        if (snackbarMessage != null) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                    actionLabel = retryLabel
                )
                if (result == SnackbarResult.ActionPerformed) {
                    onRefresh()
                }
                onSnackbarDismissed()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.news_toolbar_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(
                        onClick = onRefresh,
                        modifier = Modifier.semantics {
                            contentDescription = "Refrescar noticias"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is NewsUiState.Loading -> {
                    NewsShimmerList(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is NewsUiState.Content -> {
                    NewsContentList(
                        state = uiState,
                        onRefresh = onRefresh,
                        onLoadMore = onLoadMore,
                        onStoryClick = onStoryClick,
                        onRetryPagination = onLoadMore
                    )
                }

                is NewsUiState.Error -> {
                    NewsErrorContent(
                        message = uiState.message,
                        onRetry = onRetry,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is NewsUiState.Empty -> {
                    NewsEmptyContent(
                        onRefresh = onRefresh,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsContentList(
    state: NewsUiState.Content,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onStoryClick: (StoryUiModel) -> Unit,
    onRetryPagination: () -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 5 &&
                state.paginationState == PaginationState.Idle
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(
                items = state.stories,
                key = { _, story -> story.id }
            ) { index, story ->
                NewsItemCard(
                    story = story,
                    onClick = { onStoryClick(story) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (index < state.stories.lastIndex) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

            when (state.paginationState) {
                is PaginationState.Loading -> {
                    item {
                        PaginationLoadingItem(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is PaginationState.Error -> {
                    item {
                        PaginationErrorItem(
                            message = state.paginationState.message,
                            onRetry = onRetryPagination,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is PaginationState.Idle,
                is PaginationState.AllLoaded -> { /* Sin indicador */ }
            }
        }
    }
}

// region Previews

@Preview(showBackground = true)
@Composable
private fun NewsScreenLoadingPreview() {
    HackersNewTheme(dynamicColor = false) {
        NewsScreen(
            uiState = NewsUiState.Loading,
            onRefresh = {},
            onLoadMore = {},
            onStoryClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsScreenContentPreview() {
    HackersNewTheme(dynamicColor = false) {
        NewsScreen(
            uiState = NewsUiState.Content(
                stories = previewStories(),
                isRefreshing = false,
                paginationState = PaginationState.Idle
            ),
            onRefresh = {},
            onLoadMore = {},
            onStoryClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsScreenErrorPreview() {
    HackersNewTheme(dynamicColor = false) {
        NewsScreen(
            uiState = NewsUiState.Error(message = "No se pudo conectar al servidor"),
            onRefresh = {},
            onLoadMore = {},
            onStoryClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsScreenEmptyPreview() {
    HackersNewTheme(dynamicColor = false) {
        NewsScreen(
            uiState = NewsUiState.Empty,
            onRefresh = {},
            onLoadMore = {},
            onStoryClick = {},
            onRetry = {}
        )
    }
}

private fun previewStories() = listOf(
    StoryUiModel(
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
    StoryUiModel(
        id = 2,
        position = 2,
        title = "The Architecture of Open Source Applications",
        score = 189,
        author = "pg",
        commentCount = 43,
        domain = "arxiv.org",
        relativeTime = "hace 5h",
        url = "https://arxiv.org/abs/1234.5678"
    ),
    StoryUiModel(
        id = 3,
        position = 3,
        title = "Ask HN: What are you working on?",
        score = 312,
        author = "someone",
        commentCount = 256,
        domain = "news.ycombinator.com",
        relativeTime = "hace 1d",
        url = "https://news.ycombinator.com/item?id=3"
    )
)

// endregion
