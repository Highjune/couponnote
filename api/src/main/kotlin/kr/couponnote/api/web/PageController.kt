package kr.couponnote.api.web

import kr.couponnote.application.article.ArticleQueryService
import kr.couponnote.application.brand.BrandQueryService
import kr.couponnote.application.coupon.CouponQueryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class PageController(
    private val brandQueryService: BrandQueryService,
    private val couponQueryService: CouponQueryService,
    private val articleQueryService: ArticleQueryService,
) {
    @GetMapping("/")
    fun home(model: Model): String {
        model.addAttribute("sections", brandQueryService.sections())
        model.addAttribute("canonical", "/")
        return "home"
    }

    /** 사이트 소개·제휴 문의. 리터럴 경로라 /{brand} 보다 우선 매칭 */
    @GetMapping("/about")
    fun about(model: Model): String {
        model.addAttribute("canonical", "/about")
        return "about"
    }

    /** 브랜드 메인: /myrealtrip — 쿠폰 섹션 + 보조 글 목록 */
    @GetMapping("/{brand:[a-z0-9-]+}")
    fun brand(@PathVariable brand: String, model: Model): String {
        val page = couponQueryService.brandPage(brand)
        model.addAttribute("page", page)
        model.addAttribute("articles", articleQueryService.listByBrand(brand))
        model.addAttribute("canonical", "/$brand")
        return "brand"
    }

    /** 보조 글: /myrealtrip/new-member */
    @GetMapping("/{brand:[a-z0-9-]+}/{slug:[a-z0-9-]+}")
    fun article(@PathVariable brand: String, @PathVariable slug: String, model: Model): String {
        model.addAttribute("article", articleQueryService.get(brand, slug))
        model.addAttribute("canonical", "/$brand/$slug")
        return "article"
    }
}
