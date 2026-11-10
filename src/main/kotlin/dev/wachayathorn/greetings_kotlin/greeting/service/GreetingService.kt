package dev.wachayathorn.greetings_kotlin.greeting.service

import dev.wachayathorn.greetings_kotlin.greeting.dto.request.UpsertGreetingRequest
import dev.wachayathorn.greetings_kotlin.greeting.dto.response.GreetingResponse
import dev.wachayathorn.greetings_kotlin.greeting.exception.GreetingNotFoundException
import dev.wachayathorn.greetings_kotlin.greeting.exception.InvalidGreetingIdException
import dev.wachayathorn.greetings_kotlin.greeting.model.Greeting
import dev.wachayathorn.greetings_kotlin.greeting.normalizedOrNull
import dev.wachayathorn.greetings_kotlin.greeting.repository.GreetingRepository
import dev.wachayathorn.greetings_kotlin.greeting.toResponse
import org.springframework.stereotype.Service

@Service
class GreetingService(
    private val greetingRepository: GreetingRepository,
) {
    fun create(request: UpsertGreetingRequest): GreetingResponse {
        val greeting =
            Greeting(
                id = greetingRepository.nextId(),
                message = request.message.trim(),
                recipient = request.recipient.normalizedOrNull(),
            )
        return greetingRepository.save(greeting).toResponse()
    }

    fun list(): List<GreetingResponse> = greetingRepository.findAll().map { it.toResponse() }

    // when แยกกรณี null กับเจอของ ?: คือทางสั้นของ when ที่เหลือค่าเดียวถ้าซ้ายเป็น null
    fun get(id: Long): GreetingResponse {
        requirePositive(id)
        return when (val found = greetingRepository.findById(id)) {
            null -> throw GreetingNotFoundException(id)
            else -> found.toResponse()
        }
    }

    // copy สร้าง data class ใหม่โดยเปลี่ยนเฉพาะฟิลด์ที่ระบุ ที่เหลือคงค่าเดิม
    fun update(
        id: Long,
        request: UpsertGreetingRequest,
    ): GreetingResponse {
        requirePositive(id)
        val existing = greetingRepository.findById(id) ?: throw GreetingNotFoundException(id)
        val updated =
            existing.copy(
                message = request.message.trim(),
                recipient = request.recipient.normalizedOrNull(),
            )
        return greetingRepository.save(updated).toResponse()
    }

    fun delete(id: Long) {
        requirePositive(id)
        val removed = greetingRepository.deleteById(id)
        if (!removed) {
            throw GreetingNotFoundException(id)
        }
    }

    private fun requirePositive(id: Long) {
        if (id <= 0) {
            throw InvalidGreetingIdException()
        }
    }
}
