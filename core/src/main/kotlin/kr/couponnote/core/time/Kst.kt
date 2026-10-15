package kr.couponnote.core.time

import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

object Kst {
    val zone: ZoneId = ZoneId.of("Asia/Seoul")

    /** 테스트에서 교체 가능하도록 Clock 주입 지점 */
    @Volatile
    var clock: Clock = Clock.system(zone)

    fun today(): LocalDate = LocalDate.now(clock)

    /** "2026년 10월" 같은 제목용 월 표기 */
    fun monthLabel(date: LocalDate = today()): String = "${date.year}년 ${date.monthValue}월"
}
