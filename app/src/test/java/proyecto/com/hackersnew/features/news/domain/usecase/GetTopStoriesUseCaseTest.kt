package proyecto.com.hackersnew.features.news.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import proyecto.com.hackersnew.features.news.domain.model.Story
import proyecto.com.hackersnew.features.news.domain.repository.NewsRepository
import java.io.IOException

class GetTopStoriesUseCaseTest {

    private lateinit var repository: NewsRepository
    private lateinit var useCase: GetTopStoriesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetTopStoriesUseCase(repository)
    }

    @Test
    fun `should return first page of stories when API returns IDs`() = runTest {
        val ids = (1L..50L).toList()
        val stories = (1L..20L).map { createStory(it) }

        coEvery { repository.getTopStoryIds(any()) } returns Result.success(ids)
        coEvery { repository.getStories(ids.take(20)) } returns Result.success(stories)

        val result = useCase(page = 0)

        assertTrue(result.isSuccess)
        assertEquals(20, result.getOrThrow().stories.size)
        assertTrue(result.getOrThrow().hasMore)
    }

    @Test
    fun `should return second page with correct IDs`() = runTest {
        val ids = (1L..50L).toList()
        val page2Stories = (21L..40L).map { createStory(it) }

        coEvery { repository.getTopStoryIds(any()) } returns Result.success(ids)
        coEvery { repository.getStories(ids.drop(20).take(20)) } returns Result.success(page2Stories)

        val result = useCase(page = 1)

        assertTrue(result.isSuccess)
        assertEquals(20, result.getOrThrow().stories.size)
        assertTrue(result.getOrThrow().hasMore)
    }

    @Test
    fun `should return hasMore false when on last page`() = runTest {
        val ids = (1L..25L).toList()
        val lastPageStories = (21L..25L).map { createStory(it) }

        coEvery { repository.getTopStoryIds(any()) } returns Result.success(ids)
        coEvery { repository.getStories(ids.drop(20).take(20)) } returns Result.success(lastPageStories)

        val result = useCase(page = 1)

        assertTrue(result.isSuccess)
        assertEquals(5, result.getOrThrow().stories.size)
        assertFalse(result.getOrThrow().hasMore)
    }

    @Test
    fun `should return empty list with hasMore false when page exceeds total`() = runTest {
        val ids = (1L..20L).toList()

        coEvery { repository.getTopStoryIds(any()) } returns Result.success(ids)

        val result = useCase(page = 1)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().stories.isEmpty())
        assertFalse(result.getOrThrow().hasMore)
    }

    @Test
    fun `should return failure when getTopStoryIds fails`() = runTest {
        coEvery { repository.getTopStoryIds(any()) } returns Result.failure(IOException("Sin conexión"))

        val result = useCase(page = 0)

        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when getStories fails`() = runTest {
        val ids = (1L..50L).toList()

        coEvery { repository.getTopStoryIds(any()) } returns Result.success(ids)
        coEvery { repository.getStories(any()) } returns Result.failure(IOException("Timeout"))

        val result = useCase(page = 0)

        assertTrue(result.isFailure)
    }

    @Test
    fun `should pass forceRefresh to repository`() = runTest {
        val ids = (1L..20L).toList()
        val stories = (1L..20L).map { createStory(it) }

        coEvery { repository.getTopStoryIds(forceRefresh = true) } returns Result.success(ids)
        coEvery { repository.getStories(any()) } returns Result.success(stories)

        useCase(page = 0, forceRefresh = true)

        coVerify { repository.getTopStoryIds(forceRefresh = true) }
    }

    @Test
    fun `should return empty result for empty ID list`() = runTest {
        coEvery { repository.getTopStoryIds(any()) } returns Result.success(emptyList())

        val result = useCase(page = 0)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().stories.isEmpty())
        assertFalse(result.getOrThrow().hasMore)
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