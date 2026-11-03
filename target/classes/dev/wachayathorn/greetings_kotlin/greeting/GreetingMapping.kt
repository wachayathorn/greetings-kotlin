package dev.wachayathorn.greetings_kotlin.greeting

import dev.wachayathorn.greetings_kotlin.greeting.dto.response.GreetingResponse
import dev.wachayathorn.greetings_kotlin.greeting.model.Greeting

// ฟังก์ชันต่อท้ายคลาสที่มีอยู่แล้ว Java ต้องทำคลาส util แยก
fun Greeting.toResponse(): GreetingResponse = GreetingResponse(
	id = id,
	message = message,
	recipient = recipient,
)

// let ทำงานในบล็อกเมื่อไม่ใช่ null แล้วคืนค่าจากบล็อก takeIf คืน null ถ้าเงื่อนไขไม่ผ่าน
fun String?.normalizedOrNull(): String? = this?.let { it.trim() }?.takeIf(String::isNotEmpty)
