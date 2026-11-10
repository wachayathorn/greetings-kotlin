package dev.wachayathorn.greetings_kotlin.greeting

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.ResultHandler
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
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
        mockMvc
            .performAwait(
                post("/api/v1/greetings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"message":"  hello  ","recipient":"  Ann  "}"""),
            ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.message").value("hello"))
            .andExpect(jsonPath("$.recipient").value("Ann"))

        mockMvc
            .performAwait(
                post("/api/v1/greetings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"message":"no one"}"""),
            ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.recipient").doesNotExist())

        mockMvc
            .performAwait(get("/api/v1/greetings"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))

        mockMvc
            .performAwait(get("/api/v1/greetings/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("hello"))

        mockMvc
            .performAwait(
                put("/api/v1/greetings/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"message":"updated","recipient":"   "}"""),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("updated"))
            .andExpect(jsonPath("$.recipient").doesNotExist())

        mockMvc
            .performAwait(delete("/api/v1/greetings/1"))
            .andExpect(status().isNoContent)

        mockMvc
            .performAwait(get("/api/v1/greetings/1"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.id").value(1))
    }

    @Test
    fun rejectsBlankMessage() {
        mockMvc
            .performAwait(
                post("/api/v1/greetings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"message":"   "}"""),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun rejectsNonPositiveId() {
        mockMvc
            .performAwait(get("/api/v1/greetings/0"))
            .andExpect(status().isBadRequest)
    }

    // suspend controller ตอบแบบ async ใน MockMvc ต้องรอ asyncDispatch ก่อนอ่านผล
    private fun MockMvc.performAwait(builder: RequestBuilder): ResultActions {
        val mvcResult = perform(builder).andReturn()
        return if (mvcResult.request.isAsyncStarted) {
            perform(asyncDispatch(mvcResult))
        } else {
            CompletedResultActions(mvcResult)
        }
    }

    private class CompletedResultActions(
        private val mvcResult: MvcResult,
    ) : ResultActions {
        override fun andExpect(matcher: ResultMatcher): ResultActions {
            matcher.match(mvcResult)
            return this
        }

        override fun andExpectAll(vararg matchers: ResultMatcher): ResultActions {
            matchers.forEach { it.match(mvcResult) }
            return this
        }

        override fun andDo(handler: ResultHandler): ResultActions {
            handler.handle(mvcResult)
            return this
        }

        override fun andReturn(): MvcResult = mvcResult
    }
}
