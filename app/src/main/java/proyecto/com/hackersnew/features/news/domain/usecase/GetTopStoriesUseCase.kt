package proyecto.com.hackersnew.features.news.domain.usecase

import proyecto.com.hackersnew.features.news.domain.model.TopStoriesResult
import proyecto.com.hackersnew.features.news.domain.repository.NewsRepository

class GetTopStoriesUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int = PAGE_SIZE,
        forceRefresh: Boolean = false
    ): Result<TopStoriesResult> {
        val idsResult = repository.getTopStoryIds(forceRefresh)
        if (idsResult.isFailure) {
            return Result.failure(idsResult.exceptionOrNull()!!)
        }

        val allIds = idsResult.getOrThrow()
        val startIndex = page * pageSize
        val pageIds = allIds.drop(startIndex).take(pageSize)

        if (pageIds.isEmpty()) {
            return Result.success(TopStoriesResult(stories = emptyList(), hasMore = false))
        }

        val storiesResult = repository.getStories(pageIds)
        if (storiesResult.isFailure) {
            return Result.failure(storiesResult.exceptionOrNull()!!)
        }

        val stories = storiesResult.getOrThrow()
        val hasMore = startIndex + pageSize < allIds.size

        return Result.success(TopStoriesResult(stories = stories, hasMore = hasMore))
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}