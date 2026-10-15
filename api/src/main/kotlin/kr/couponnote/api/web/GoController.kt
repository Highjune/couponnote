package kr.couponnote.api.web

import jakarta.servlet.http.HttpServletRequest
import kr.couponnote.application.click.ClickContext
import kr.couponnote.application.click.ClickService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

/**
 * 모든 마이링크 버튼은 /go/{couponId} 를 거친다.
 * 클릭을 기록하고 302로 마이링크(myrealt.rip/...)에 보낸다.
 * 검색엔진이 따라가지 않도록 noindex + nofollow 헤더.
 */
@Controller
class GoController(private val clickService: ClickService) {
    @GetMapping("/go/{couponId}")
    fun go(@PathVariable couponId: Long, request: HttpServletRequest): ResponseEntity<Void> {
        val target = clickService.record(
            couponId,
            ClickContext(
                path = request.getHeader(HttpHeaders.REFERER)?.let { refererPath(it) } ?: "/",
                referer = request.getHeader(HttpHeaders.REFERER),
                userAgent = request.getHeader(HttpHeaders.USER_AGENT),
                ip = request.getHeader("X-Real-IP") ?: request.remoteAddr,
            ),
        )
        return ResponseEntity.status(HttpStatus.FOUND)
            .header(HttpHeaders.LOCATION, target)
            .header("X-Robots-Tag", "noindex, nofollow")
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .build()
    }

    private fun refererPath(referer: String): String =
        runCatching { java.net.URI(referer).path.ifBlank { "/" } }.getOrDefault("/")
}
