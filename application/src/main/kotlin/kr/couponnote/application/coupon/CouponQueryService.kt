package kr.couponnote.application.coupon

import kr.couponnote.application.brand.BrandQueryService
import kr.couponnote.core.time.Kst
import kr.couponnote.domain.coupon.CouponKind
import kr.couponnote.domain.coupon.CouponRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

data class CouponView(
    val id: Long,
    val title: String,
    val code: String?,
    val benefit: String,
    val condition: String?,
    val endsAt: LocalDate?,
    val featured: Boolean,
)

data class CouponSection(val kind: CouponKind, val label: String, val coupons: List<CouponView>)

data class BrandPage(
    val slug: String,
    val name: String,
    val tagline: String,
    val monthLabel: String,
    val sections: List<CouponSection>,
)

@Service
@Transactional(readOnly = true)
class CouponQueryService(
    private val brandQueryService: BrandQueryService,
    private val couponRepository: CouponRepository,
) {
    /** 브랜드 메인 페이지 전체. 날짜가 키라서 자정에 자연 갱신 */
    @Cacheable("brandPage", key = "#slug")
    fun brandPage(slug: String): BrandPage {
        val brand = brandQueryService.getBySlug(slug)
        val today = Kst.today()
        val sections = couponRepository.findLiveByBrand(brand.id, today)
            .groupBy { it.kind }
            .toSortedMap(compareBy { it.ordinal })
            .map { (kind, list) ->
                CouponSection(
                    kind = kind,
                    label = kind.label,
                    coupons = list.map { CouponView(it.id, it.title, it.code, it.benefit, it.condition, it.endsAt, it.featured) },
                )
            }
        return BrandPage(brand.slug, brand.name, brand.tagline, Kst.monthLabel(today), sections)
    }
}
