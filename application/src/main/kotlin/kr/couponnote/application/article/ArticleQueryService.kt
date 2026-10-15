package kr.couponnote.application.article

import kr.couponnote.core.exception.NotFoundException
import kr.couponnote.domain.article.ArticleRepository
import kr.couponnote.infrastructure.markdown.MarkdownRenderer
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

data class ArticleView(
    val brandSlug: String,
    val brandName: String,
    val slug: String,
    val title: String,
    val description: String,
    val bodyHtml: String,
    val updatedAt: LocalDateTime,
)

data class ArticleSummary(val slug: String, val title: String, val description: String, val updatedAt: LocalDateTime)

@Service
@Transactional(readOnly = true)
class ArticleQueryService(
    private val articleRepository: ArticleRepository,
    private val markdownRenderer: MarkdownRenderer,
) {
    @Cacheable("article", key = "#brandSlug + '/' + #slug")
    fun get(brandSlug: String, slug: String): ArticleView {
        val a = articleRepository.findByBrandSlugAndSlugAndPublishedTrue(brandSlug, slug)
            ?: throw NotFoundException("article: $brandSlug/$slug")
        return ArticleView(
            brandSlug = a.brand.slug,
            brandName = a.brand.name,
            slug = a.slug,
            title = a.title,
            description = a.description,
            bodyHtml = markdownRenderer.toHtml(a.bodyMarkdown),
            updatedAt = a.updatedAt,
        )
    }

    @Cacheable("articleList", key = "#brandSlug")
    fun listByBrand(brandSlug: String): List<ArticleSummary> =
        articleRepository.findAllByBrandSlugAndPublishedTrueOrderByUpdatedAtDesc(brandSlug)
            .map { ArticleSummary(it.slug, it.title, it.description, it.updatedAt) }
}
