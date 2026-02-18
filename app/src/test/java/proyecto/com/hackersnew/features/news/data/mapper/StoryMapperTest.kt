package proyecto.com.hackersnew.features.news.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import proyecto.com.hackersnew.features.news.data.remote.dto.StoryDto

class StoryMapperTest {

    @Test
    fun `should map all fields correctly when DTO has all values`() {
        val dto = StoryDto(
            id = 8863,
            title = "My YC app: Dropbox",
            by = "dhouston",
            score = 104,
            descendants = 71,
            time = 1175714200L,
            url = "http://www.getdropbox.com/u/2/screencast.html",
            type = "story",
            kids = listOf(9224, 8917)
        )

        val story = dto.toDomain()

        assertEquals(8863L, story.id)
        assertEquals("My YC app: Dropbox", story.title)
        assertEquals("dhouston", story.author)
        assertEquals(104, story.score)
        assertEquals(71, story.commentCount)
        assertEquals(1175714200L, story.timestamp)
        assertEquals("http://www.getdropbox.com/u/2/screencast.html", story.url)
        assertEquals("story", story.type)
    }

    @Test
    fun `should use empty string for null title`() {
        val dto = createDto(title = null)
        assertEquals("", dto.toDomain().title)
    }

    @Test
    fun `should use empty string for null author`() {
        val dto = createDto(by = null)
        assertEquals("", dto.toDomain().author)
    }

    @Test
    fun `should use 0 for null score`() {
        val dto = createDto(score = null)
        assertEquals(0, dto.toDomain().score)
    }

    @Test
    fun `should use 0 for null descendants`() {
        val dto = createDto(descendants = null)
        assertEquals(0, dto.toDomain().commentCount)
    }

    @Test
    fun `should use 0 for null time`() {
        val dto = createDto(time = null)
        assertEquals(0L, dto.toDomain().timestamp)
    }

    @Test
    fun `should preserve null url`() {
        val dto = createDto(url = null)
        assertEquals(null, dto.toDomain().url)
    }

    @Test
    fun `should use empty string for null type`() {
        val dto = createDto(type = null)
        assertEquals("", dto.toDomain().type)
    }

    private fun createDto(
        id: Long = 1L,
        title: String? = "Title",
        by: String? = "author",
        score: Int? = 10,
        descendants: Int? = 5,
        time: Long? = 1000L,
        url: String? = "https://example.com",
        type: String? = "story",
        kids: List<Long>? = null
    ) = StoryDto(
        id = id,
        title = title,
        by = by,
        score = score,
        descendants = descendants,
        time = time,
        url = url,
        type = type,
        kids = kids
    )
}