package kr.couponnote.dto

import java.time.LocalDate

/** GET /api/v1/brands/{slug} 응답 */
data class BrandCouponsResponse(
    val slug: String,
    val name: String,
    val tagline: String,
    val monthLabel: String,
    val sections: List<CouponSectionDto>,
)

data class CouponSectionDto(
    val kind: String,
    val label: String,
    val coupons: List<CouponDto>,
)

data class CouponDto(
    val id: Long,
    val title: String,
    val code: String?,
    val benefit: String,
    val condition: String?,
    val endsAt: LocalDate?,
    /** 클릭 기록을 거치는 이동 경로. 소비자는 이 URL 로 보내야 전환이 집계된다 */
    val goPath: String,
)
