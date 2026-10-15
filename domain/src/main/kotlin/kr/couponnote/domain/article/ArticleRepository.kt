package kr.couponnote.domain.article

import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long> {
    fun findByBrandSlugAndSlugAndPublishedTrue(brandSlug: String, slug: String): Article?
    fun findAllByBrandSlugAndPublishedTrueOrderByUpdatedAtDesc(brandSlug: String): List<Article>
}
