package proyecto.com.hackersnew.features.news.data.remote.dto

import com.google.gson.annotations.SerializedName

data class StoryDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String?,
    @SerializedName("by") val by: String?,
    @SerializedName("score") val score: Int?,
    @SerializedName("descendants") val descendants: Int?,
    @SerializedName("time") val time: Long?,
    @SerializedName("url") val url: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("kids") val kids: List<Long>?
)