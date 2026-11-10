package dev.wachayathorn.greetings_kotlin.greeting.repository

import dev.wachayathorn.greetings_kotlin.greeting.model.Greeting
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class GreetingRepository {
    private val greetings = ConcurrentHashMap<Long, Greeting>()
    private val sequence = AtomicLong(0)

    fun nextId(): Long = sequence.incrementAndGet()

    // also ทำงานกับ object แล้วคืน object เดิม ใช้บันทึกแล้วส่ง greeting กลับไปให้คนเรียก
    fun save(greeting: Greeting): Greeting = greeting.also { greetings[it.id] = it }

    fun findById(id: Long): Greeting? = greetings[id]

    fun findAll(): List<Greeting> = greetings.values.sortedBy { it.id }

    fun deleteById(id: Long): Boolean = greetings.remove(id) != null
}
