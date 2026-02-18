package proyecto.com.hackersnew

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import proyecto.com.hackersnew.core.network.RetrofitProvider
import proyecto.com.hackersnew.features.news.data.remote.HackerNewsApiService
import proyecto.com.hackersnew.features.news.data.repository.NewsRepositoryImpl
import proyecto.com.hackersnew.features.news.domain.usecase.GetTopStoriesUseCase
import proyecto.com.hackersnew.features.news.presentation.NewsScreen
import proyecto.com.hackersnew.features.news.presentation.NewsViewModel
import proyecto.com.hackersnew.ui.theme.HackersNewTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val apiService = RetrofitProvider.createService<HackerNewsApiService>()
        val repository = NewsRepositoryImpl(apiService)
        val getTopStoriesUseCase = GetTopStoriesUseCase(repository)
        val viewModelFactory = NewsViewModel.Factory(getTopStoriesUseCase)

        setContent {
            HackersNewTheme {
                val newsViewModel: NewsViewModel = viewModel(factory = viewModelFactory)
                val uiState by newsViewModel.uiState.collectAsStateWithLifecycle()
                val snackbarMessage by newsViewModel.snackbarMessage.collectAsStateWithLifecycle()

                NewsScreen(
                    uiState = uiState,
                    onRefresh = newsViewModel::refresh,
                    onLoadMore = newsViewModel::loadMore,
                    onStoryClick = { story ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(story.url))
                        startActivity(intent)
                    },
                    onRetry = newsViewModel::loadInitial,
                    snackbarMessage = snackbarMessage,
                    onSnackbarDismissed = newsViewModel::dismissSnackbar
                )
            }
        }
    }
}
