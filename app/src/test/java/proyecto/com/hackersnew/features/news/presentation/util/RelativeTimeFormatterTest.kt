package proyecto.com.hackersnew.features.news.presentation.util

import org.junit.Assert.assertEquals
import org.junit.Test

class RelativeTimeFormatterTest {

    private val nowMillis = 1700000000000L // Timestamp fijo para tests

    @Test
    fun `should return hace 1m for less than 60 seconds`() {
        val timestamp = (nowMillis / 1000) - 30 // 30 segundos atrás
        assertEquals("hace 1m", RelativeTimeFormatter.format(timestamp, nowMillis))
    }

    @Test
    fun `should return minutes for less than 1 hour`() {
        val timestamp = (nowMillis / 1000) - (15 * 60) // 15 minutos atrás
        assertEquals("hace 15m", RelativeTimeFormatter.format(timestamp, nowMillis))
    }

    @Test
    fun `should return hours for less than 1 day`() {
        val timestamp = (nowMillis / 1000) - (3 * 3600) // 3 horas atrás
        assertEquals("hace 3h", RelativeTimeFormatter.format(timestamp, nowMillis))
    }

    @Test
    fun `should return days for 1 or more days`() {
        val timestamp = (nowMillis / 1000) - (2 * 86400) // 2 días atrás
        assertEquals("hace 2d", RelativeTimeFormatter.format(timestamp, nowMillis))
    }

    @Test
    fun `should return hace 1m for exactly 0 seconds`() {
        val timestamp = nowMillis / 1000
        assertEquals("hace 1m", RelativeTimeFormatter.format(timestamp, nowMillis))
    }

    @Test
    fun `should return hace 59m for 59 minutes`() {
        val timestamp = (nowMillis / 1000) - (59 * 60)
        assertEquals("hace 59m", RelativeTimeFormatter.format(timestamp, nowMillis))
    }

    @Test
    fun `should return hace 1h for exactly 60 minutes`() {
        val timestamp = (nowMillis / 1000) - (60 * 60)
        assertEquals("hace 1h", RelativeTimeFormatter.format(timestamp, nowMillis))
    }

    @Test
    fun `should return hace 23h for 23 hours`() {
        val timestamp = (nowMillis / 1000) - (23 * 3600)
        assertEquals("hace 23h", RelativeTimeFormatter.format(timestamp, nowMillis))
    }

    @Test
    fun `should return hace 1d for exactly 24 hours`() {
        val timestamp = (nowMillis / 1000) - (24 * 3600)
        assertEquals("hace 1d", RelativeTimeFormatter.format(timestamp, nowMillis))
    }

    @Test
    fun `should return large day count for old timestamps`() {
        val timestamp = (nowMillis / 1000) - (365 * 86400L) // 1 año atrás
        assertEquals("hace 365d", RelativeTimeFormatter.format(timestamp, nowMillis))
    }
}