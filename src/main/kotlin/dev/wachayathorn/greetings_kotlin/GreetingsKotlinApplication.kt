package dev.wachayathorn.greetings_kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GreetingsKotlinApplication

fun main(args: Array<String>) {
	runApplication<GreetingsKotlinApplication>(*args)
}
