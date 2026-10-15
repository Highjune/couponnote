package kr.couponnote.domain.article

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import kr.couponnote.domain.brand.Brand
import kr.couponnote.domain.common.BaseEntity

/**
 * 보조 글. URL = /{brand.slug}/{slug}. 본문은 마크다운.
 * 브랜드 메인(/myrealtrip)은 글이 아니라 쿠폰 데이터로 렌더하므로 여기 없음.
 */
@Entity
@Table(
    name = "articles",
    uniqueConstraints = [UniqueConstraint(name = "uk_articles_brand_slug", columnNames = ["brand_id", "slug"])],
)
class Article(
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id")
    val brand: Brand,

    @Column(nullable = false, length = 80)
    val slug: String,

    @Column(nullable = false, length = 150)
    var title: String,

    /** meta description. 160자 이내 */
    @Column(nullable = false, length = 160)
    var description: String,

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    var bodyMarkdown: String,

    @Column(nullable = false)
    var published: Boolean = false,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
