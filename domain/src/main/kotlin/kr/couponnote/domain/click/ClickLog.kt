package kr.couponnote.domain.click

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * 마이링크 버튼 클릭 1건. 파트너페이지 "링크 ID별 수익"과 couponId→targetUrl 로 조인해 전환율 계산.
 * 개인정보: IP는 해시만 저장.
 */
@Entity
@Table(
    name = "click_logs",
    indexes = [
        Index(name = "idx_click_logs_coupon_at", columnList = "coupon_id,clicked_at"),
        Index(name = "idx_click_logs_at", columnList = "clicked_at"),
    ],
)
class ClickLog(
    @Column(name = "coupon_id", nullable = false)
    val couponId: Long,

    /** 클릭이 발생한 페이지 경로. /myrealtrip */
    @Column(nullable = false, length = 200)
    val path: String,

    @Column(length = 500)
    val referer: String?,

    @Column(length = 300)
    val userAgent: String?,

    @Column(nullable = false, length = 64)
    val ipHash: String,

    @Column(name = "clicked_at", nullable = false)
    val clickedAt: LocalDateTime,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
