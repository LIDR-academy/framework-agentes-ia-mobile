package proyecto.com.hackersnew.features.news.data.mapper

import proyecto.com.hackersnew.features.news.data.remote.dto.StoryDto
import proyecto.com.hackersnew.features.news.domain.model.Story

fun StoryDto.toDomain(): Story {
    return Story(
        id = id,
        title = title.orEmpty(),
        author = by.orEmpty(),
        score = score ?: 0,
        commentCount = descendants ?: 0,
        timestamp = time ?: 0L,
        url = url,
        type = type.orEmpty()
    )
}