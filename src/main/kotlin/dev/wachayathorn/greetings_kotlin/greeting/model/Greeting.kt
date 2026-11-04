package dev.wachayathorn.greetings_kotlin.greeting.model

// data class สร้าง equals, hashCode, toString และ copy ให้เอง ใน Java ต้องเขียนมือหรือใช้ record
data class Greeting(
    val id: Long,
    val message: String,
    // String? คือรับค่า null ได้ Java ใช้ @Nullable หรือไม่บอกอะไรเลย
    val recipient: String?,
)
