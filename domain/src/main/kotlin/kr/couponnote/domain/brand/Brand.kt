package kr.couponnote.domain.brand

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kr.couponnote.domain.common.BaseEntity

/** 홈에서 브랜드를 묶는 단위. 선언 순서가 홈 섹션 순서 */
enum class BrandCategory(val label: String, val description: String, val emoji: String) {
    TRAVEL("여행 쿠폰", "숙박·투어·항공까지, 여행을 더 저렴하게", "✈️"),
    SHOPPING("쇼핑 할인코드", "국내외 쇼핑몰 최신 쿠폰", "🛍️"),
    SUBSCRIPTION("구독 서비스 쿠폰", "게임·콘텐츠·앱 구독을 더 싸게", "🎮"),
}

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var category: BrandCategory,

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
