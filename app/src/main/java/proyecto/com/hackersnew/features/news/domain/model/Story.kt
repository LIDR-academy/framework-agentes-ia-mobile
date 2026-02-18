package proyecto.com.hackersnew.features.news.domain.model

data class Story(
    val id: Long,
    val title: String,
    val author: String,
    val score: Int,
    val commentCount: Int,
    val timestamp: Long,
    val url: String?,
    val type: String
)