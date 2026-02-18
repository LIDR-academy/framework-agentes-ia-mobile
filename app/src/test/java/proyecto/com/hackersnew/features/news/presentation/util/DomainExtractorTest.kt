package proyecto.com.hackersnew.features.news.presentation.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DomainExtractorTest {

    @Test
    fun `should extract domain from standard URL`() {
        assertEquals("github.com", DomainExtractor.extract("https://github.com/user/repo"))
    }

    @Test
    fun `should remove www prefix`() {
        assertEquals("getdropbox.com", DomainExtractor.extract("http://www.getdropbox.com/u/2/screencast.html"))
    }

    @Test
    fun `should preserve subdomain that is not www`() {
        assertEquals("blog.example.com", DomainExtractor.extract("https://blog.example.com/post"))
    }

    @Test
    fun `should return default domain for null URL`() {
        assertEquals("news.ycombinator.com", DomainExtractor.extract(null))
    }

    @Test
    fun `should return default domain for empty URL`() {
        assertEquals("news.ycombinator.com", DomainExtractor.extract(""))
    }

    @Test
    fun `should return default domain for blank URL`() {
        assertEquals("news.ycombinator.com", DomainExtractor.extract("   "))
    }

    @Test
    fun `should return default domain for invalid URL`() {
        assertEquals("news.ycombinator.com", DomainExtractor.extract("not a url"))
    }

    @Test
    fun `should handle URL with port`() {
        assertEquals("localhost", DomainExtractor.extract("http://localhost:8080/path"))
    }

    @Test
    fun `should handle complex TLD`() {
        assertEquals("example.co.uk", DomainExtractor.extract("https://example.co.uk/page"))
    }

    @Test
    fun `should handle arxiv URL`() {
        assertEquals("arxiv.org", DomainExtractor.extract("https://arxiv.org/abs/1234.5678"))
    }
}