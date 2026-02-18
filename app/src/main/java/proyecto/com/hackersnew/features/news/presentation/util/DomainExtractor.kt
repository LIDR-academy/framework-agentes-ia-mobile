package proyecto.com.hackersnew.features.news.presentation.util

import java.net.URI

object DomainExtractor {

    private const val DEFAULT_DOMAIN = "news.ycombinator.com"
    private const val WWW_PREFIX = "www."

    fun extract(url: String?): String {
        if (url.isNullOrBlank()) return DEFAULT_DOMAIN

        return try {
            val host = URI(url).host ?: return DEFAULT_DOMAIN
            if (host.startsWith(WWW_PREFIX)) {
                host.removePrefix(WWW_PREFIX)
            } else {
                host
            }
        } catch (_: Exception) {
            DEFAULT_DOMAIN
        }
    }
}