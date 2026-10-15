package kr.couponnote.client

import kr.couponnote.dto.BrandCouponsResponse
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient

class RestCouponnoteClient(
    private val baseUrl: String,
    private val restClient: RestClient,
) : CouponnoteClient {
    override fun getBrand(slug: String): BrandCouponsResponse? =
        try {
            restClient.get()
                .uri("/api/v1/brands/{slug}", slug)
                .retrieve()
                .body(BrandCouponsResponse::class.java)
        } catch (e: HttpClientErrorException.NotFound) {
            null
        }

    override fun absoluteUrl(path: String): String = baseUrl.trimEnd('/') + path
}
