package kr.couponnote.api.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "couponnote")
data class SiteProperties(
    val siteUrl: String,
    val disclosure: String,
)
