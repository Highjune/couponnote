package kr.couponnote.application.click

import kr.couponnote.core.exception.NotFoundException
import kr.couponnote.core.time.Kst
import kr.couponnote.domain.click.ClickLog
import kr.couponnote.domain.click.ClickLogRepository
import kr.couponnote.domain.coupon.CouponRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.LocalDateTime

data class ClickContext(val path: String, val referer: String?, val userAgent: String?, val ip: String)

@Service
class ClickService(
    private val couponRepository: CouponRepository,
    private val clickLogRepository: ClickLogRepository,
) {
    /** 클릭 기록 후 이동할 마이링크 반환. 쿠폰이 없거나 비활성이면 404 */
    @Transactional
    fun record(couponId: Long, ctx: ClickContext): String {
        val coupon = couponRepository.findById(couponId).orElseThrow { NotFoundException("coupon: $couponId") }
        if (!coupon.active) throw NotFoundException("coupon inactive: $couponId")
        clickLogRepository.save(
            ClickLog(
                couponId = coupon.id,
                path = ctx.path.take(200),
                referer = ctx.referer?.take(500),
                userAgent = ctx.userAgent?.take(300),
                ipHash = sha256(ctx.ip),
                clickedAt = LocalDateTime.now(Kst.clock),
            ),
        )
        return coupon.targetUrl
    }

    private fun sha256(s: String): String =
        MessageDigest.getInstance("SHA-256").digest(s.toByteArray()).joinToString("") { "%02x".format(it) }
}
