package kr.couponnote.domain.coupon

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface CouponRepository : JpaRepository<Coupon, Long> {
    @Query(
        """
        select c from Coupon c
        where c.brand.id = :brandId
          and c.active = true
          and (c.startsAt is null or c.startsAt <= :today)
          and (c.endsAt is null or c.endsAt >= :today)
        order by c.kind asc, c.sortOrder asc, c.id asc
        """,
    )
    fun findLiveByBrand(brandId: Long, today: LocalDate): List<Coupon>

    fun countByBrandIdAndActiveTrue(brandId: Long): Long

    /** 활성 브랜드 전체의 오늘 유효 쿠폰. 상단 상태바(유효 개수·마감 임박)용 */
    @Query(
        """
        select c from Coupon c join fetch c.brand b
        where b.active = true
          and c.active = true
          and (c.startsAt is null or c.startsAt <= :today)
          and (c.endsAt is null or c.endsAt >= :today)
        """,
    )
    fun findLiveAll(today: LocalDate): List<Coupon>
}
