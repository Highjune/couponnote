package kr.couponnote.domain.brand

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kr.couponnote.domain.common.BaseEntity

/**
 * 쿠폰을 다루는 플랫폼. URL 1단계(/myrealtrip)가 slug.
 */
@Entity
@Table(name = "brands")
class Brand(
    @Column(nullable = false, unique = true, length = 40)
    val slug: String,

    @Column(nullable = false, length = 60)
    var name: String,

    /** 검색 제목에 쓰는 짧은 소개. 예: "투어·항공·숙소 예약" */
    @Column(nullable = false, length = 120)
    var tagline: String,

    @Column(nullable = false)
    var sortOrder: Int = 0,

    @Column(nullable = false)
    var active: Boolean = true,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
