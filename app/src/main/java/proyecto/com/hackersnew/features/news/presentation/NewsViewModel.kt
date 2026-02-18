package proyecto.com.hackersnew.features.news.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import proyecto.com.hackersnew.features.news.domain.model.Story
import proyecto.com.hackersnew.features.news.domain.usecase.GetTopStoriesUseCase
import proyecto.com.hackersnew.features.news.presentation.model.StoryUiModel
import proyecto.com.hackersnew.features.news.presentation.util.DomainExtractor
import proyecto.com.hackersnew.features.news.presentation.util.RelativeTimeFormatter

class NewsViewModel(
    private val getTopStoriesUseCase: GetTopStoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    private var currentPage = 0
    private var allLoadedStories = mutableListOf<StoryUiModel>()

    init {
        loadInitial()
    }

    fun loadInitial() {
        _uiState.value = NewsUiState.Loading
        currentPage = 0
        allLoadedStories.clear()
        loadPage(page = 0)
    }

    fun refresh() {
        val currentState = _uiState.value
        if (currentState is NewsUiState.Content && currentState.isRefreshing) return

        if (currentState is NewsUiState.Content) {
            _uiState.update {
                (it as? NewsUiState.Content)?.copy(isRefreshing = true) ?: it
            }
        }

        viewModelScope.launch {
            val result = getTopStoriesUseCase(page = 0, forceRefresh = true)
            result.fold(
                onSuccess = { topStoriesResult ->
                    currentPage = 0
                    allLoadedStories.clear()
                    val uiModels = topStoriesResult.stories.mapToUiModels(startPosition = 1)
                    allLoadedStories.addAll(uiModels)
                    currentPage = 1

                    _uiState.value = when {
                        allLoadedStories.isEmpty() -> NewsUiState.Empty
                        else -> NewsUiState.Content(
                            stories = allLoadedStories.toList(),
                            isRefreshing = false,
                            paginationState = if (topStoriesResult.hasMore) PaginationState.Idle else PaginationState.AllLoaded
                        )
                    }
                },
                onFailure = {
                    if (currentState is NewsUiState.Content) {
                        _uiState.update {
                            (it as? NewsUiState.Content)?.copy(isRefreshing = false) ?: it
                        }
                        _snackbarMessage.value = "No se pudo refrescar. Intenta de nuevo."
                    } else {
                        _uiState.value = NewsUiState.Error("No se pudo cargar las noticias. Verifica tu conexión a internet.")
                    }
                }
            )
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (currentState !is NewsUiState.Content) return
        if (currentState.paginationState != PaginationState.Idle) return

        _uiState.update {
            (it as? NewsUiState.Content)?.copy(paginationState = PaginationState.Loading) ?: it
        }

        loadPage(page = currentPage)
    }

    fun dismissSnackbar() {
        _snackbarMessage.value = null
    }

    private fun loadPage(page: Int) {
        viewModelScope.launch {
            val result = getTopStoriesUseCase(page = page, forceRefresh = page == 0 && allLoadedStories.isEmpty())
            result.fold(
                onSuccess = { topStoriesResult ->
                    val startPosition = allLoadedStories.size + 1
                    val uiModels = topStoriesResult.stories.mapToUiModels(startPosition)
                    allLoadedStories.addAll(uiModels)
                    currentPage = page + 1

                    _uiState.value = when {
                        allLoadedStories.isEmpty() -> NewsUiState.Empty
                        else -> NewsUiState.Content(
                            stories = allLoadedStories.toList(),
                            paginationState = if (topStoriesResult.hasMore) PaginationState.Idle else PaginationState.AllLoaded
                        )
                    }
                },
                onFailure = { error ->
                    if (allLoadedStories.isEmpty()) {
                        _uiState.value = NewsUiState.Error(
                            "No se pudo cargar las noticias. Verifica tu conexión a internet."
                        )
                    } else {
                        _uiState.update {
                            (it as? NewsUiState.Content)?.copy(
                                paginationState = PaginationState.Error("Error al cargar más noticias")
                            ) ?: it
                        }
                    }
                }
            )
        }
    }

    private fun List<Story>.mapToUiModels(startPosition: Int): List<StoryUiModel> {
        return mapIndexed { index, story ->
            StoryUiModel(
                id = story.id,
                position = startPosition + index,
                title = story.title,
                score = story.score,
                author = story.author,
                commentCount = story.commentCount,
                domain = DomainExtractor.extract(story.url),
                relativeTime = RelativeTimeFormatter.format(story.timestamp),
                url = story.url ?: "https://news.ycombinator.com/item?id=${story.id}"
            )
        }
    }

    class Factory(
        private val getTopStoriesUseCase: GetTopStoriesUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NewsViewModel(getTopStoriesUseCase) as T
        }
    }
}