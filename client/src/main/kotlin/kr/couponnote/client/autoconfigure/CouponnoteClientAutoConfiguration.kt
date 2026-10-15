package kr.couponnote.client.autoconfigure

import kr.couponnote.client.CouponnoteClient
import kr.couponnote.client.RestCouponnoteClient
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.time.Duration

@ConfigurationProperties(prefix = "couponnote.client")
data class CouponnoteClientProperties(
    /** 예: https://couponnote.kr */
    val baseUrl: String,
    val connectTimeout: Duration = Duration.ofSeconds(2),
    val readTimeout: Duration = Duration.ofSeconds(3),
)

/**
 * 소비자 앱에 couponnote.client.base-url 만 있으면 CouponnoteClient 빈이 자동 등록된다.
 * 직접 빈을 정의하면 그쪽이 우선.
 */
@AutoConfiguration
@EnableConfigurationProperties(CouponnoteClientProperties::class)
@ConditionalOnProperty(prefix = "couponnote.client", name = ["base-url"])
class CouponnoteClientAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun couponnoteClient(props: CouponnoteClientProperties): CouponnoteClient {
        val factory = JdkClientHttpRequestFactory().apply { setReadTimeout(props.readTimeout) }
        val restClient = RestClient.builder()
            .baseUrl(props.baseUrl)
            .requestFactory(factory)
            .defaultHeader("User-Agent", "couponnote-client")
            .build()
        return RestCouponnoteClient(props.baseUrl, restClient)
    }
}
