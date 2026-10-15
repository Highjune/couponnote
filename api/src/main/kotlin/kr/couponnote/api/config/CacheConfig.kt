package kr.couponnote.api.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

/**
 * 페이지 단위 캐시. 사용자별 내용이 없어 100% 적중.
 * admin에서 수정 시 CacheManager.getCache(...).clear() 로 무효화 (admin 구현 시).
 */
@Configuration
class CacheConfig {
    @Bean
    fun cacheManager(): CacheManager =
        CaffeineCacheManager("brandCards", "brandPage", "article", "articleList").apply {
            setCaffeine(Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).maximumSize(500))
        }
}
