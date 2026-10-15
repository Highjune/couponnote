package kr.couponnote.application.brand

import kr.couponnote.core.exception.NotFoundException
import kr.couponnote.domain.brand.Brand
import kr.couponnote.domain.brand.BrandRepository
import kr.couponnote.domain.coupon.CouponRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class BrandCard(val slug: String, val name: String, val tagline: String, val liveCouponCount: Long)

@Service
@Transactional(readOnly = true)
class BrandQueryService(
    private val brandRepository: BrandRepository,
    private val couponRepository: CouponRepository,
) {
    @Cacheable("brandCards")
    fun cards(): List<BrandCard> =
        brandRepository.findAllByActiveTrueOrderBySortOrderAsc().map {
            BrandCard(it.slug, it.name, it.tagline, couponRepository.countByBrandIdAndActiveTrue(it.id))
        }

    fun getBySlug(slug: String): Brand =
        brandRepository.findBySlugAndActiveTrue(slug) ?: throw NotFoundException("brand: $slug")
}
