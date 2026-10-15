package kr.couponnote.api

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class PageSmokeTest(@Autowired private val mvc: MockMvc) {
    @Test
    fun `home renders brand cards`() {
        mvc.get("/").andExpect {
            status { isOk() }
            content { string(org.hamcrest.Matchers.containsString("마이리얼트립")) }
        }
    }

    @Test
    fun `brand page hides expired coupons and shows disclosure`() {
        mvc.get("/myrealtrip").andExpect {
            status { isOk() }
            content { string(org.hamcrest.Matchers.containsString("마케팅 파트너십")) }
            content { string(org.hamcrest.Matchers.containsString("EXAMPLE10")) }
            content { string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("만료된 코드"))) }
        }
    }

    @Test
    fun `go redirects to target and is noindex`() {
        mvc.get("/go/1").andExpect {
            status { isFound() }
            header { exists("Location") }
            header { string("X-Robots-Tag", "noindex, nofollow") }
        }
    }

    @Test
    fun `unknown brand is 404`() {
        mvc.get("/nope").andExpect { status { isNotFound() } }
    }
}

