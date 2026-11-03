package dev.wachayathorn.greetings_kotlin.greeting.exception

class GreetingNotFoundException(
	val id: Long,
) : RuntimeException("Greeting $id was not found")
