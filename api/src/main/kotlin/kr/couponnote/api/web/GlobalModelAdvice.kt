package kr.couponnote.api.web

import kr.couponnote.api.config.SiteProperties
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

/** 모든 템플릿에서 쓰는 값. 템플릿에서 빈을 직접 부르지 않도록 모델로 내려준다. */
@ControllerAdvice
class GlobalModelAdvice(private val site: SiteProperties) {
    @ModelAttribute("siteUrl")
    fun siteUrl(): String = site.siteUrl

    @ModelAttribute("disclosure")
    fun disclosure(): String = site.disclosure
}
