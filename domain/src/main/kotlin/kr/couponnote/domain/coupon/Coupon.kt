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
enum class CouponKind(val label: String) {
    PACK("쿠폰팩"),
    CODE("할인코드"),
    CARD("카드사 즉시할인"),
    REGION("여행지별 쿠폰"),
    FLIGHT("항공권 프로모션"),
    EVENT("특가·이벤트"),
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
