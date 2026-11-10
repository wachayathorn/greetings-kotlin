package dev.wachayathorn.greetings_kotlin.greeting.controller

import dev.wachayathorn.greetings_kotlin.greeting.dto.request.UpsertGreetingRequest
import dev.wachayathorn.greetings_kotlin.greeting.dto.response.GreetingResponse
import dev.wachayathorn.greetings_kotlin.greeting.service.GreetingService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// คลาสนี้รับ GreetingService ผ่าน constructor Spring สร้างให้ ไม่ต้องใส่ @Autowired
@RestController
@RequestMapping("/api/v1/greetings")
class GreetingController(
    private val greetingService: GreetingService,
) {
    // suspend ที่ controller ให้ Spring รอ coroutine จบ แล้วค่อยตอบ HTTP
    @PostMapping
    suspend fun create(
        @Valid @RequestBody request: UpsertGreetingRequest,
    ): ResponseEntity<GreetingResponse> = ResponseEntity.status(HttpStatus.CREATED).body(greetingService.create(request))

    @GetMapping
    suspend fun list(): ResponseEntity<List<GreetingResponse>> = ResponseEntity.ok(greetingService.list())

    @GetMapping("/{id}")
    suspend fun get(
        @PathVariable id: Long,
    ): ResponseEntity<GreetingResponse> = ResponseEntity.ok(greetingService.get(id))

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpsertGreetingRequest,
    ): ResponseEntity<GreetingResponse> = ResponseEntity.ok(greetingService.update(id, request))

    @DeleteMapping("/{id}")
    suspend fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        greetingService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
