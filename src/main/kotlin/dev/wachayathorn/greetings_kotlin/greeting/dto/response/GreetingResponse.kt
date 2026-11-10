package dev.wachayathorn.greetings_kotlin.greeting.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

// ข้ามฟิลด์ที่เป็น null ดังนั้น recipient ที่ไม่มีจะไม่โผล่ใน JSON
@JsonInclude(JsonInclude.Include.NON_NULL)
data class GreetingResponse(
    val id: Long,
    val message: String,
    val recipient: String?,
)
