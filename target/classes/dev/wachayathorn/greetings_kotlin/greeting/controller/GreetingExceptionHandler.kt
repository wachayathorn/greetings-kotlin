package dev.wachayathorn.greetings_kotlin.greeting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

import dev.wachayathorn.greetings_kotlin.greeting.exception.GreetingNotFoundException

@RestControllerAdvice
class GreetingExceptionHandler {

	@ExceptionHandler(GreetingNotFoundException::class)
	fun missing(exception: GreetingNotFoundException): ProblemDetail =
		// apply ตั้งค่าบน object แล้วคืน object นั้น ProblemDetail ใน Java มักเรียก setter แยกบรรทัด
		ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Greeting not found").apply {
			setProperty("id", exception.id)
		}

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun invalid(exception: MethodArgumentNotValidException): ProblemDetail {
		// filter เก็บเฉพาะแถวที่ผ่านเงื่อนไข map แปลงแต่ละแถวเป็นค่าใหม่
		val errors = exception.bindingResult.fieldErrors
			.filter { it.defaultMessage != null }
			.map { "${it.field}: ${it.defaultMessage}" }
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request validation failed").apply {
			setProperty("errors", errors)
		}
	}
}
