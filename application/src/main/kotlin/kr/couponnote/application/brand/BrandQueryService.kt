package kr.couponnote.application.brand

import kr.couponnote.core.exception.NotFoundException
import kr.couponnote.domain.brand.Brand
import kr.couponnote.domain.brand.BrandCategory
import kr.couponnote.domain.brand.BrandRepository
import kr.couponnote.domain.coupon.CouponRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class BrandCard(val slug: String, val name: String, val tagline: String, val liveCouponCount: Long)

/** 홈 한 섹션 = 카테고리 하나. 브랜드가 없는 카테고리는 제외 */
data class BrandCategorySection(val category: BrandCategory, val brands: List<BrandCard>)

@Service
@Transactional(readOnly = true)
class BrandQueryService(
    private val brandRepository: BrandRepository,
    private val couponRepository: CouponRepository,
) {
    @Cacheable("brandCards")
    fun sections(): List<BrandCategorySection> {
        val byCategory = brandRepository.findAllByActiveTrueOrderBySortOrderAsc()
            .groupBy({ it.category }) { BrandCard(it.slug, it.name, it.tagline, couponRepository.countByBrandIdAndActiveTrue(it.id)) }
        return BrandCategory.entries.mapNotNull { c -> byCategory[c]?.let { BrandCategorySection(c, it) } }
    }

    fun getBySlug(slug: String): Brand =
        brandRepository.findBySlugAndActiveTrue(slug) ?: throw NotFoundException("brand: $slug")
}
