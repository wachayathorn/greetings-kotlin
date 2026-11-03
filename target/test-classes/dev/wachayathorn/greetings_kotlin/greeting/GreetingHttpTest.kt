package dev.wachayathorn.greetings_kotlin.greeting

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class GreetingHttpTest {

	@Autowired
	lateinit var mockMvc: MockMvc

	@Test
	fun createListGetUpdateAndDelete() {
		mockMvc.perform(
			post("/api/v1/greetings")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""{"message":"  hello  ","recipient":"  Ann  "}"""),
		)
			.andExpect(status().isCreated)
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.message").value("hello"))
			.andExpect(jsonPath("$.recipient").value("Ann"))

		mockMvc.perform(
			post("/api/v1/greetings")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""{"message":"no one"}"""),
		)
			.andExpect(status().isCreated)
			.andExpect(jsonPath("$.recipient").doesNotExist())

		mockMvc.perform(get("/api/v1/greetings"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.length()").value(2))

		mockMvc.perform(get("/api/v1/greetings/1"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.message").value("hello"))

		mockMvc.perform(
			put("/api/v1/greetings/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""{"message":"updated","recipient":"   "}"""),
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.message").value("updated"))
			.andExpect(jsonPath("$.recipient").doesNotExist())

		mockMvc.perform(delete("/api/v1/greetings/1"))
			.andExpect(status().isNoContent)

		mockMvc.perform(get("/api/v1/greetings/1"))
			.andExpect(status().isNotFound)
			.andExpect(jsonPath("$.id").value(1))
	}

	@Test
	fun rejectsBlankMessage() {
		mockMvc.perform(
			post("/api/v1/greetings")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""{"message":"   "}"""),
		)
			.andExpect(status().isBadRequest)
	}

	@Test
	fun rejectsNonPositiveId() {
		mockMvc.perform(get("/api/v1/greetings/0"))
			.andExpect(status().isBadRequest)
	}
}
