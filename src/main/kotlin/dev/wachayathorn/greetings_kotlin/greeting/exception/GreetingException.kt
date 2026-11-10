package dev.wachayathorn.greetings_kotlin.greeting.exception

import org.springframework.http.ProblemDetail

// abstract class สืบทอดได้โดยไม่ต้องใส่ open มีได้ทั้ง logic ร่วมและเมธอดที่ลูกต้องเขียน
abstract class GreetingException(
    message: String,
) : RuntimeException(message) {
    // ลูกแต่ละชนิดตอบ HTTP คนละแบบ handler เรียกเมธอดนี้ผ่านชนิดแม่ได้
    abstract fun toProblemDetail(): ProblemDetail
}
