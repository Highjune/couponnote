package kr.couponnote.domain.coupon

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import kr.couponnote.domain.brand.Brand
import kr.couponnote.domain.common.BaseEntity
import java.time.LocalDate

/** 페이지 섹션 구분. 순서가 페이지 섹션 순서 */
enum class CouponKind(val label: String, val emoji: String, val description: String) {
    PACK("쿠폰팩", "🎁", "버튼 한 번으로 여러 장이 한 번에 발급되는 쿠폰팩입니다. 코드 입력이 필요 없습니다."),
    CODE("할인코드", "🤝", "결제 화면에 입력하는 파트너 전용 코드입니다. 금액대에 따라 유리한 쪽 하나만 고르세요."),
    CARD("카드사 즉시할인", "💳", "해당 카드로 결제하면 자동 적용됩니다. 할인코드와 중복됩니다."),
    REGION("여행지별 쿠폰", "🗺️", "특정 여행지 상품에만 적용되는 쿠폰입니다."),
    FLIGHT("항공권 프로모션", "✈️", "이번 달 특가 노선과 항공권 혜택입니다."),
    EVENT("특가·이벤트", "🔥", "기간 한정 이벤트와 제휴 혜택입니다."),
}

@Entity
@Table(
    name = "coupons",
    indexes = [Index(name = "idx_coupons_brand_kind", columnList = "brand_id,kind,sort_order")],
)
class Coupon(
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id")
    val brand: Brand,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var kind: CouponKind,

    @Column(nullable = false, length = 120)
    var title: String,

    /** 입력용 코드. 없으면 null (쿠폰팩·카드할인) */
    @Column(length = 60)
    var code: String? = null,

    /** "보홀 20%", "최대 5,000원" */
    @Column(nullable = false, length = 120)
    var benefit: String,

    /** 조건. "15만원 이상, 1인 1회" */
    @Column(length = 300)
    var condition: String? = null,

    var startsAt: LocalDate? = null,

    /** null = 상시 */
    var endsAt: LocalDate? = null,

    /** 마이링크 (myrealt.rip/...). 버튼은 /go/{id} 를 거쳐 여기로 302 */
    @Column(nullable = false, length = 300)
    var targetUrl: String,

    @Column(nullable = false)
    var sortOrder: Int = 0,

    /** 섹션 안에서 강조(BEST 뱃지·강조 테두리). 보통 금액이 가장 큰 쿠폰 1개 */
    @Column(nullable = false)
    var featured: Boolean = false,

    @Column(nullable = false)
    var active: Boolean = true,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun isLiveOn(date: LocalDate): Boolean =
        active &&
            (startsAt == null || !date.isBefore(startsAt)) &&
            (endsAt == null || !date.isAfter(endsAt))
}
