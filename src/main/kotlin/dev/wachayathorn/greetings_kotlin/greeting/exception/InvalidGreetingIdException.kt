package dev.wachayathorn.greetings_kotlin.greeting.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

class InvalidGreetingIdException : GreetingException("Greeting id must be positive") {

	override fun toProblemDetail(): ProblemDetail =
		ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message!!)
}
