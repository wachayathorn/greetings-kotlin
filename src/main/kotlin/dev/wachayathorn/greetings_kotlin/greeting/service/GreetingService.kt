package dev.wachayathorn.greetings_kotlin.greeting.service

import dev.wachayathorn.greetings_kotlin.greeting.dto.request.UpsertGreetingRequest
import dev.wachayathorn.greetings_kotlin.greeting.dto.response.GreetingResponse
import dev.wachayathorn.greetings_kotlin.greeting.exception.GreetingNotFoundException
import dev.wachayathorn.greetings_kotlin.greeting.exception.InvalidGreetingIdException
import dev.wachayathorn.greetings_kotlin.greeting.model.Greeting
import dev.wachayathorn.greetings_kotlin.greeting.normalizedOrNull
import dev.wachayathorn.greetings_kotlin.greeting.repository.GreetingRepository
import dev.wachayathorn.greetings_kotlin.greeting.toResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class GreetingService(
    private val greetingRepository: GreetingRepository,
) {
    // suspend = ฟังก์ชันหยุดรอได้โดยไม่บล็อกเธรดแบบ Thread.sleep
    // withContext(Dispatchers.IO) ย้ายงาน IO ไปพูลที่เหมาะกับรอ I/O
    suspend fun create(request: UpsertGreetingRequest): GreetingResponse = withContext(Dispatchers.IO) {
        val greeting =
            Greeting(
                id = greetingRepository.nextId(),
                message = request.message.trim(),
                recipient = request.recipient.normalizedOrNull(),
            )
        greetingRepository.save(greeting).toResponse()
    }

    suspend fun list(): List<GreetingResponse> = withContext(Dispatchers.IO) {
        greetingRepository.findAll().map { it.toResponse() }
    }

    // coroutineScope + async รันงานย่อยพร้อมกัน แล้ว await รวมผล
    suspend fun get(id: Long): GreetingResponse = coroutineScope {
        requirePositive(id)
        val found =
            async(Dispatchers.IO) {
                greetingRepository.findById(id)
            }
        when (val greeting = found.await()) {
            null -> throw GreetingNotFoundException(id)
            else -> greeting.toResponse()
        }
    }

    // copy สร้าง data class ใหม่โดยเปลี่ยนเฉพาะฟิลด์ที่ระบุ ที่เหลือคงค่าเดิม
    suspend fun update(
        id: Long,
        request: UpsertGreetingRequest,
    ): GreetingResponse = withContext(Dispatchers.IO) {
        requirePositive(id)
        val existing = greetingRepository.findById(id) ?: throw GreetingNotFoundException(id)
        val updated =
            existing.copy(
                message = request.message.trim(),
                recipient = request.recipient.normalizedOrNull(),
            )
        greetingRepository.save(updated).toResponse()
    }

    suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
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
