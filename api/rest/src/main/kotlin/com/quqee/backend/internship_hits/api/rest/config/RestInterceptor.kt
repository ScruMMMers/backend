package com.quqee.backend.internship_hits.api.rest.config

import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.HashMap

@ControllerAdvice
class RestInterceptor : ResponseEntityExceptionHandler() {
    companion object {
        private val STATUS_MAP = mapOf(
            ExceptionType.IDEMPOTENT_OPERATION_FAILED to HttpStatus.CONFLICT,
            ExceptionType.NOT_FOUND to HttpStatus.NOT_FOUND,
            ExceptionType.FORBIDDEN to HttpStatus.FORBIDDEN,
            ExceptionType.FATAL to HttpStatus.INTERNAL_SERVER_ERROR,
            ExceptionType.CONFLICT to HttpStatus.CONFLICT,
            ExceptionType.BAD_REQUEST to HttpStatus.BAD_REQUEST
        )
        private val log = LoggerFactory.getLogger(RestInterceptor::class.java)
    }

    @ExceptionHandler(ExceptionInApplication::class)
    protected fun handleExceptionInApplication(ex: ExceptionInApplication, request: WebRequest): ResponseEntity<Any>? {
        log.error("Exception in application", ex)
        val status = STATUS_MAP[ex.type] ?: HttpStatus.INTERNAL_SERVER_ERROR
        return handleExceptionInternal(ex, ex.message, HttpHeaders(), status, request)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleAllException(ex: Exception, request: WebRequest): ResponseEntity<Any>? {
        log.error("Exception in application", ex)
        return handleExceptionInternal(ex, ex.message, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errors = ex.bindingResult.fieldErrors.mapNotNull { obj: FieldError -> obj.defaultMessage }
        return handleExceptionInternal(ex, getErrorsMap(errors), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    private fun getErrorsMap(errors: List<String>): Map<String, List<String>> {
        val errorResponse: MutableMap<String, List<String>> = HashMap()
        errorResponse["errors"] = errors
        return errorResponse
    }
}