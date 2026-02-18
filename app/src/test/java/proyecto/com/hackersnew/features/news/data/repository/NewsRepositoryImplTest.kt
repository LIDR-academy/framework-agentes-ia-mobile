package proyecto.com.hackersnew.features.news.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import proyecto.com.hackersnew.features.news.data.remote.HackerNewsApiService
import proyecto.com.hackersnew.features.news.data.remote.dto.StoryDto
import retrofit2.Response
import java.io.IOException

class NewsRepositoryImplTest {

    private lateinit var apiService: HackerNewsApiService
    private lateinit var repository: NewsRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        repository = NewsRepositoryImpl(apiService)
    }

    // region getTopStoryIds

    @Test
    fun `should return IDs when API returns 200`() = runTest {
        val ids = listOf(1L, 2L, 3L)
        coEvery { apiService.getTopStories() } returns Response.success(ids)

        val result = repository.getTopStoryIds()

        assertTrue(result.isSuccess)
        assertEquals(ids, result.getOrThrow())
    }

    @Test
    fun `should cache IDs on subsequent calls`() = runTest {
        val ids = listOf(1L, 2L, 3L)
        coEvery { apiService.getTopStories() } returns Response.success(ids)

        repository.getTopStoryIds()
        val result = repository.getTopStoryIds()

        assertTrue(result.isSuccess)
        assertEquals(ids, result.getOrThrow())
    }

    @Test
    fun `should refresh IDs when forceRefresh is true`() = runTest {
        val oldIds = listOf(1L, 2L)
        val newIds = listOf(3L, 4L, 5L)

        coEvery { apiService.getTopStories() } returns Response.success(oldIds) andThen Response.success(newIds)

        repository.getTopStoryIds()
        val result = repository.getTopStoryIds(forceRefresh = true)

        assertTrue(result.isSuccess)
        assertEquals(newIds, result.getOrThrow())
    }

    @Test
    fun `should return failure when API returns error`() = runTest {
        coEvery { apiService.getTopStories() } returns Response.error(500, "Server Error".toResponseBody())

        val result = repository.getTopStoryIds()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    @Test
    fun `should return NetworkException when IOException occurs`() = runTest {
        coEvery { apiService.getTopStories() } throws IOException("Sin conexión")

        val result = repository.getTopStoryIds()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NetworkException)
    }

    // endregion

    // region getStories

    @Test
    fun `should return stories for given IDs`() = runTest {
        val dto1 = createStoryDto(1L)
        val dto2 = createStoryDto(2L)

        coEvery { apiService.getStoryDetail(1L) } returns Response.success(dto1)
        coEvery { apiService.getStoryDetail(2L) } returns Response.success(dto2)

        val result = repository.getStories(listOf(1L, 2L))

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
        assertEquals(1L, result.getOrThrow()[0].id)
        assertEquals(2L, result.getOrThrow()[1].id)
    }

    @Test
    fun `should filter out items that are not stories`() = runTest {
        val storyDto = createStoryDto(1L, type = "story")
        val jobDto = createStoryDto(2L, type = "job")

        coEvery { apiService.getStoryDetail(1L) } returns Response.success(storyDto)
        coEvery { apiService.getStoryDetail(2L) } returns Response.success(jobDto)

        val result = repository.getStories(listOf(1L, 2L))

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
        assertEquals(1L, result.getOrThrow()[0].id)
    }

    @Test
    fun `should skip items that fail to load`() = runTest {
        val dto1 = createStoryDto(1L)

        coEvery { apiService.getStoryDetail(1L) } returns Response.success(dto1)
        coEvery { apiService.getStoryDetail(2L) } throws IOException("Timeout")

        val result = repository.getStories(listOf(1L, 2L))

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
    }

    @Test
    fun `should use cache for already loaded stories`() = runTest {
        val dto = createStoryDto(1L)
        coEvery { apiService.getStoryDetail(1L) } returns Response.success(dto)

        repository.getStories(listOf(1L))
        val result = repository.getStories(listOf(1L))

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
    }

    @Test
    fun `should skip items with HTTP error response`() = runTest {
        val dto1 = createStoryDto(1L)

        coEvery { apiService.getStoryDetail(1L) } returns Response.success(dto1)
        coEvery { apiService.getStoryDetail(2L) } returns Response.error(404, "Not Found".toResponseBody())

        val result = repository.getStories(listOf(1L, 2L))

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
    }

    // endregion

    private fun createStoryDto(
        id: Long,
        type: String = "story"
    ) = StoryDto(
        id = id,
        title = "Story $id",
        by = "author$id",
        score = id.toInt() * 10,
        descendants = id.toInt(),
        time = 1700000000L + id,
        url = "https://example.com/$id",
        type = type,
        kids = null
    )
}
