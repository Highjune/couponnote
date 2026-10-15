package kr.couponnote.client

import kr.couponnote.dto.BrandCouponsResponse

/**
 * 소비자가 주입받는 인터페이스. 테스트에서는 이 타입을 가짜로 바꿔 끼운다.
 */
interface CouponnoteClient {
    /** 브랜드의 현재 유효 쿠폰. 브랜드가 없으면 null */
    fun getBrand(slug: String): BrandCouponsResponse?

    /** 상대 경로(goPath)를 절대 URL 로. 소비자 화면의 버튼에 넣을 값 */
    fun absoluteUrl(path: String): String
}
