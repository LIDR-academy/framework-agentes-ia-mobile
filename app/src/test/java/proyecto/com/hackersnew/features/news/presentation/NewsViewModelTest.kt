package proyecto.com.hackersnew.features.news.presentation

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import proyecto.com.hackersnew.features.news.domain.model.Story
import proyecto.com.hackersnew.features.news.domain.model.TopStoriesResult
import proyecto.com.hackersnew.features.news.domain.usecase.GetTopStoriesUseCase
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private lateinit var getTopStoriesUseCase: GetTopStoriesUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getTopStoriesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit Loading then Content on successful initial load`() = runTest {
        val stories = (1L..20L).map { createStory(it) }
        coEvery { getTopStoriesUseCase(page = 0, forceRefresh = true) } returns
            Result.success(TopStoriesResult(stories, hasMore = true))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(NewsUiState.Loading, awaitItem())
            advanceUntilIdle()
            val content = awaitItem()
            assertTrue(content is NewsUiState.Content)
            assertEquals(20, (content as NewsUiState.Content).stories.size)
            assertEquals(PaginationState.Idle, content.paginationState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Loading then Error on failed initial load`() = runTest {
        coEvery { getTopStoriesUseCase(page = 0, forceRefresh = true) } returns
            Result.failure(IOException("Sin conexión"))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(NewsUiState.Loading, awaitItem())
            advanceUntilIdle()
            val error = awaitItem()
            assertTrue(error is NewsUiState.Error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Empty when API returns empty list`() = runTest {
        coEvery { getTopStoriesUseCase(page = 0, forceRefresh = true) } returns
            Result.success(TopStoriesResult(emptyList(), hasMore = false))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(NewsUiState.Loading, awaitItem())
            advanceUntilIdle()
            assertEquals(NewsUiState.Empty, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should load more stories on loadMore`() = runTest {
        val page1Stories = (1L..20L).map { createStory(it) }
        val page2Stories = (21L..40L).map { createStory(it) }

        coEvery { getTopStoriesUseCase(page = 0, forceRefresh = true) } returns
            Result.success(TopStoriesResult(page1Stories, hasMore = true))
        coEvery { getTopStoriesUseCase(page = 1, forceRefresh = false) } returns
            Result.success(TopStoriesResult(page2Stories, hasMore = true))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.loadMore()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is NewsUiState.Content)
        assertEquals(40, (state as NewsUiState.Content).stories.size)
    }

    @Test
    fun `should set AllLoaded when no more pages`() = runTest {
        val stories = (1L..20L).map { createStory(it) }

        coEvery { getTopStoriesUseCase(page = 0, forceRefresh = true) } returns
            Result.success(TopStoriesResult(stories, hasMore = false))

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is NewsUiState.Content)
        assertEquals(PaginationState.AllLoaded, (state as NewsUiState.Content).paginationState)
    }

    @Test
    fun `should set PaginationError when loadMore fails`() = runTest {
        val stories = (1L..20L).map { createStory(it) }

        coEvery { getTopStoriesUseCase(page = 0, forceRefresh = true) } returns
            Result.success(TopStoriesResult(stories, hasMore = true))
        coEvery { getTopStoriesUseCase(page = 1, forceRefresh = false) } returns
            Result.failure(IOException("Timeout"))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.loadMore()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is NewsUiState.Content)
        assertTrue((state as NewsUiState.Content).paginationState is PaginationState.Error)
    }

    @Test
    fun `should show snackbar on refresh error with existing content`() = runTest {
        val stories = (1L..20L).map { createStory(it) }

        coEvery { getTopStoriesUseCase(page = 0, forceRefresh = true) } returns
            Result.success(TopStoriesResult(stories, hasMore = true))
        coEvery { getTopStoriesUseCase(page = 0, forceRefresh = true) } returns
            Result.success(TopStoriesResult(stories, hasMore = true)) andThen
            Result.failure(IOException("Sin conexión"))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.refresh()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is NewsUiState.Content)
        assertEquals(20, (state as NewsUiState.Content).stories.size)
    }

    @Test
    fun `should assign correct positions to stories`() = runTest {
        val stories = (1L..3L).map { createStory(it) }

        coEvery { getTopStoriesUseCase(page = 0, forceRefresh = true) } returns
            Result.success(TopStoriesResult(stories, hasMore = false))

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value as NewsUiState.Content
        assertEquals(1, state.stories[0].position)
        assertEquals(2, state.stories[1].position)
        assertEquals(3, state.stories[2].position)
    }

    private fun createViewModel(): NewsViewModel {
        return NewsViewModel(getTopStoriesUseCase)
    }

    private fun createStory(id: Long) = Story(
        id = id,
        title = "Story $id",
        author = "author$id",
        score = id.toInt() * 10,
        commentCount = id.toInt(),
        timestamp = 1700000000L + id,
        url = "https://example.com/$id",
        type = "story"
    )
}
