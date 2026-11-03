package dev.wachayathorn.greetings_kotlin.greeting

import dev.wachayathorn.greetings_kotlin.greeting.dto.response.GreetingResponse
import dev.wachayathorn.greetings_kotlin.greeting.model.Greeting

// ฟังก์ชันต่อท้ายคลาสที่มีอยู่แล้ว Java ต้องทำคลาส util แยก
fun Greeting.toResponse(): GreetingResponse = GreetingResponse(
	id = id,
	message = message,
	recipient = recipient,
)

// this?. คือเรียกต่อเมื่อไม่ใช่ null takeIf คืน null ถ้าเงื่อนไขไม่ผ่าน
fun String?.normalizedOrNull(): String? = this?.trim()?.takeIf(String::isNotEmpty)
