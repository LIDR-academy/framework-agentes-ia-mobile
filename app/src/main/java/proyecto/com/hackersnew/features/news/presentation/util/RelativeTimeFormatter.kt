package proyecto.com.hackersnew.features.news.presentation.util

object RelativeTimeFormatter {

    private const val SECONDS_PER_MINUTE = 60L
    private const val SECONDS_PER_HOUR = 3600L
    private const val SECONDS_PER_DAY = 86400L

    fun format(unixTimestampSeconds: Long): String {
        val nowSeconds = System.currentTimeMillis() / 1000
        val diffSeconds = nowSeconds - unixTimestampSeconds

        return when {
            diffSeconds < SECONDS_PER_MINUTE -> "hace 1m"
            diffSeconds < SECONDS_PER_HOUR -> "hace ${diffSeconds / SECONDS_PER_MINUTE}m"
            diffSeconds < SECONDS_PER_DAY -> "hace ${diffSeconds / SECONDS_PER_HOUR}h"
            else -> "hace ${diffSeconds / SECONDS_PER_DAY}d"
        }
    }

    fun format(unixTimestampSeconds: Long, currentTimeMillis: Long): String {
        val nowSeconds = currentTimeMillis / 1000
        val diffSeconds = nowSeconds - unixTimestampSeconds

        return when {
            diffSeconds < SECONDS_PER_MINUTE -> "hace 1m"
            diffSeconds < SECONDS_PER_HOUR -> "hace ${diffSeconds / SECONDS_PER_MINUTE}m"
            diffSeconds < SECONDS_PER_DAY -> "hace ${diffSeconds / SECONDS_PER_HOUR}h"
            else -> "hace ${diffSeconds / SECONDS_PER_DAY}d"
        }
    }
}