package dev.wachayathorn.greetings_kotlin.greeting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

import dev.wachayathorn.greetings_kotlin.greeting.exception.GreetingException

// open เพราะ Spring ต้องสร้างคลาสลูกจาก advice นี้ Kotlin ทำให้คลาสเป็น final ถ้าไม่ระบุ
@RestControllerAdvice
open class GreetingExceptionHandler {

	// จับชนิดแม่ แล้วให้ลูกสร้าง ProblemDetail เองผ่าน abstract method
	@ExceptionHandler(GreetingException::class)
	fun greetingError(exception: GreetingException): ProblemDetail = exception.toProblemDetail()

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
