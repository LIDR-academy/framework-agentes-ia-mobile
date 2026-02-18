package proyecto.com.hackersnew.features.news.domain.model

data class TopStoriesResult(
    val stories: List<Story>,
    val hasMore: Boolean
)