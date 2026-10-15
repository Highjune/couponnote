package kr.couponnote.api.web

import kr.couponnote.application.coupon.CouponQueryService
import kr.couponnote.dto.BrandCouponsResponse
import kr.couponnote.dto.CouponDto
import kr.couponnote.dto.CouponSectionDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 외부 소비자용 JSON API. 응답 타입은 :dto 모듈, 호출 SDK 는 :client 모듈.
 * 페이지와 같은 서비스(캐시)를 쓰므로 데이터가 어긋나지 않는다.
 */
@RestController
@RequestMapping("/api/v1")
class ApiController(private val couponQueryService: CouponQueryService) {
    @GetMapping("/brands/{slug}")
    fun brand(@PathVariable slug: String): BrandCouponsResponse {
        val page = couponQueryService.brandPage(slug)
        return BrandCouponsResponse(
            slug = page.slug,
            name = page.name,
            tagline = page.tagline,
            monthLabel = page.monthLabel,
            sections = page.sections.map { s ->
                CouponSectionDto(
                    kind = s.kind.name,
                    label = s.label,
                    coupons = s.coupons.map {
                        CouponDto(it.id, it.title, it.code, it.benefit, it.condition, it.endsAt, "/go/${it.id}")
                    },
                )
            },
        )
    }
}
