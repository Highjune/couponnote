package kr.couponnote.domain.brand

import org.springframework.data.jpa.repository.JpaRepository

interface BrandRepository : JpaRepository<Brand, Long> {
    fun findBySlugAndActiveTrue(slug: String): Brand?
    fun findAllByActiveTrueOrderBySortOrderAsc(): List<Brand>
}
