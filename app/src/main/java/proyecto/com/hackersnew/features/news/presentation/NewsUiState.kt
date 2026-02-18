package proyecto.com.hackersnew.features.news.presentation

import proyecto.com.hackersnew.features.news.presentation.model.StoryUiModel

sealed interface NewsUiState {
    data object Loading : NewsUiState

    data class Content(
        val stories: List<StoryUiModel>,
        val isRefreshing: Boolean = false,
        val paginationState: PaginationState = PaginationState.Idle
    ) : NewsUiState

    data class Error(val message: String) : NewsUiState

    data object Empty : NewsUiState
}

sealed interface PaginationState {
    data object Idle : PaginationState
    data object Loading : PaginationState
    data class Error(val message: String) : PaginationState
    data object AllLoaded : PaginationState
}
