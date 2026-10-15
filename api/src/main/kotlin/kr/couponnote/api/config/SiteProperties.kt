package kr.couponnote.api.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "couponnote")
data class SiteProperties(
    val siteUrl: String,
    val disclosure: String,
    /** 소개·제휴 문의 주소. 템플릿에만 노출 */
    val contactEmail: String,
)
