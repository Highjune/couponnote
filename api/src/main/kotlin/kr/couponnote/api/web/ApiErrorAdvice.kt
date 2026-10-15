package kr.couponnote.api.web

import kr.couponnote.core.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/** API 경로(/api/...)는 HTML 404 페이지가 아니라 RFC 9457 problem+json 으로 응답 */
@RestControllerAdvice(assignableTypes = [ApiController::class])
class ApiErrorAdvice {
    @ExceptionHandler(NotFoundException::class)
    fun notFound(e: NotFoundException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message ?: "not found")
}
