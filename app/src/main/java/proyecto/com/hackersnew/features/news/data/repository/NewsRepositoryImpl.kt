package proyecto.com.hackersnew.features.news.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import proyecto.com.hackersnew.features.news.data.mapper.toDomain
import proyecto.com.hackersnew.features.news.data.remote.HackerNewsApiService
import proyecto.com.hackersnew.features.news.domain.model.Story
import proyecto.com.hackersnew.features.news.domain.repository.NewsRepository
import java.io.IOException

class NewsRepositoryImpl(
    private val apiService: HackerNewsApiService
) : NewsRepository {

    private var cachedIds: List<Long>? = null
    private val storyCache = mutableMapOf<Long, Story>()
    private val concurrencySemaphore = Semaphore(MAX_CONCURRENT_REQUESTS)

    override suspend fun getTopStoryIds(forceRefresh: Boolean): Result<List<Long>> {
        if (!forceRefresh) {
            cachedIds?.let { return Result.success(it) }
        }

        return try {
            val response = apiService.getTopStories()
            if (response.isSuccessful) {
                val ids = response.body().orEmpty()
                cachedIds = ids
                if (forceRefresh) {
                    storyCache.clear()
                }
                Result.success(ids)
            } else {
                Result.failure(HttpException(response.code(), response.message()))
            }
        } catch (e: IOException) {
            Result.failure(NetworkException(e))
        }
    }

    override suspend fun getStories(ids: List<Long>): Result<List<Story>> {
        return try {
            val stories = coroutineScope {
                ids.map { id ->
                    async {
                        concurrencySemaphore.withPermit {
                            fetchStory(id)
                        }
                    }
                }.awaitAll()
            }
            val successfulStories = stories.filterNotNull()
            Result.success(successfulStories)
        } catch (e: IOException) {
            Result.failure(NetworkException(e))
        }
    }

    private suspend fun fetchStory(id: Long): Story? {
        storyCache[id]?.let { return it }

        return try {
            val response = apiService.getStoryDetail(id)
            if (response.isSuccessful) {
                response.body()
                    ?.takeIf { it.type == STORY_TYPE }
                    ?.toDomain()
                    ?.also { storyCache[id] = it }
            } else {
                null
            }
        } catch (_: IOException) {
            null
        }
    }

    companion object {
        private const val MAX_CONCURRENT_REQUESTS = 10
        private const val STORY_TYPE = "story"
    }
}

class HttpException(val code: Int, override val message: String) : Exception("HTTP $code: $message")
class NetworkException(cause: Throwable) : Exception("Error de red", cause)