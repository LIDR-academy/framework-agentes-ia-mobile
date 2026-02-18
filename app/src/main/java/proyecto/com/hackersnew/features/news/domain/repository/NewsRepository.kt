package proyecto.com.hackersnew.features.news.domain.repository

import proyecto.com.hackersnew.features.news.domain.model.Story

interface NewsRepository {
    suspend fun getTopStoryIds(forceRefresh: Boolean = false): Result<List<Long>>
    suspend fun getStories(ids: List<Long>): Result<List<Story>>
}