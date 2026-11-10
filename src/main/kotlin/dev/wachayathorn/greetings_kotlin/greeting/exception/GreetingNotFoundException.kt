package dev.wachayathorn.greetings_kotlin.greeting.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

class GreetingNotFoundException(
    val id: Long,
) : GreetingException("Greeting $id was not found") {
    override fun toProblemDetail(): ProblemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, "Greeting not found")
        .apply {
            setProperty("id", id)
        }
}
