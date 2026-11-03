package dev.wachayathorn.greetings_kotlin.greeting.dto.request

import jakarta.validation.constraints.NotBlank

data class UpsertGreetingRequest(
	@field:NotBlank
	val message: String,
	val recipient: String? = null,
)
