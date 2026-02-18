package proyecto.com.hackersnew.features.news.presentation.model

data class StoryUiModel(
    val id: Long,
    val position: Int,
    val title: String,
    val score: Int,
    val author: String,
    val commentCount: Int,
    val domain: String,
    val relativeTime: String,
    val url: String
)
