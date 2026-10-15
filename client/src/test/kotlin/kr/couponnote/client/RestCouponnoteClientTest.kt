package kr.couponnote.client

import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestClient
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RestCouponnoteClientTest {
    private val builder = RestClient.builder().baseUrl("https://couponnote.kr")
    private val server = MockRestServiceServer.bindTo(builder).build()
    private val client = RestCouponnoteClient("https://couponnote.kr", builder.build())

    @Test
    fun `parses brand response and builds absolute go url`() {
        server.expect(requestTo("https://couponnote.kr/api/v1/brands/myrealtrip"))
            .andRespond(
                withSuccess(
                    """
                    {"slug":"myrealtrip","name":"마이리얼트립","tagline":"투어","monthLabel":"2026년 10월",
                     "sections":[{"kind":"CODE","label":"할인코드","coupons":[
                       {"id":7,"title":"파트너 코드","code":"ABC","benefit":"5천원","condition":null,"endsAt":"2026-10-31","goPath":"/go/7"}]}]}
                    """.trimIndent(),
                    MediaType.APPLICATION_JSON,
                ),
            )

        val res = client.getBrand("myrealtrip")!!
        assertEquals("ABC", res.sections.single().coupons.single().code)
        assertEquals("https://couponnote.kr/go/7", client.absoluteUrl(res.sections.single().coupons.single().goPath))
        server.verify()
    }

    @Test
    fun `404 becomes null`() {
        server.expect(requestTo("https://couponnote.kr/api/v1/brands/nope"))
            .andRespond(withStatus(HttpStatus.NOT_FOUND))
        assertNull(client.getBrand("nope"))
    }
}
