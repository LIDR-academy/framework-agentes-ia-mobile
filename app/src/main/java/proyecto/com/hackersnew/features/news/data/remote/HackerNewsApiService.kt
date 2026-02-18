package proyecto.com.hackersnew.features.news.data.remote

import proyecto.com.hackersnew.features.news.data.remote.dto.StoryDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HackerNewsApiService {

    @GET("v0/topstories.json")
    suspend fun getTopStories(): Response<List<Long>>

    @GET("v0/item/{id}.json")
    suspend fun getStoryDetail(@Path("id") id: Long): Response<StoryDto>
}