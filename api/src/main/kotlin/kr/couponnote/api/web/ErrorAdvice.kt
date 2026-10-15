package kr.couponnote.api.web

import kr.couponnote.core.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * 404는 반드시 HTTP 404로. (테니스 앱처럼 200 + 오류 페이지로 주면 검색엔진이 빈 페이지를 인덱싱함)
 */
@ControllerAdvice
class ErrorAdvice {
    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFound(): String = "error/404"
}
