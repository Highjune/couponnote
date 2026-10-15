package kr.couponnote.infrastructure.markdown

import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet
import org.springframework.stereotype.Component

@Component
class MarkdownRenderer {
    private val options = MutableDataSet().apply {
        set(Parser.EXTENSIONS, listOf(TablesExtension.create()))
        // 외부 입력이 아니라 admin이 쓰는 글이므로 raw HTML 허용. 공개 입력을 받게 되면 escape 켤 것.
        set(HtmlRenderer.ESCAPE_HTML, false)
    }
    private val parser = Parser.builder(options).build()
    private val renderer = HtmlRenderer.builder(options).build()

    fun toHtml(markdown: String): String = renderer.render(parser.parse(markdown))
}
