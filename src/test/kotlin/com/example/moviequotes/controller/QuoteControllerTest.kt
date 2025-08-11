package com.example.moviequotes.controller

import com.example.moviequotes.model.Quote
import com.example.moviequotes.service.QuoteService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class QuoteControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var quoteService: QuoteService  // Autowire the service

    @BeforeEach
    fun setup() {
        quoteService.reset()  // Reset before each test to clear shared state
    }

    @Test
    fun `add quote - valid`() {
        val quote = Quote(text = "Test", character = "Char", movie = "Movie", year = 2000)
        mockMvc.post("/api/quotes") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(quote)
        }.andExpect { status { isCreated() } }
    }

    @Test
    fun `add quote - invalid`() {
        val invalidQuote = Quote(text = "", character = "", movie = "", year = 1800)
        mockMvc.post("/api/quotes") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(invalidQuote)
        }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `get all quotes`() {
        mockMvc.get("/api/quotes").andExpect { status { isOk() } }
    }

    @Test
    fun `get quote by id - not found`() {
        mockMvc.get("/api/quotes/999").andExpect { status { isNotFound() } }
    }

    @Test
    fun `search quotes`() {
        addTestQuote(Quote(text = "Test", character = "SearchChar", movie = "Movie", year = 2000))
        mockMvc.get("/api/quotes/search?character=SearchChar").andExpect { status { isOk() } }
    }

    @Test
    fun `get daily quote - no content if empty`() {
        mockMvc.get("/api/quotes/daily").andExpect { status { isNoContent() } }
    }

    private fun addTestQuote(quote: Quote) {
        mockMvc.post("/api/quotes") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(quote)
        }
    }

    @Test
    fun `delete quote`() {
        val quote = Quote(text = "ToDelete", character = "Char", movie = "Movie", year = 2000)
        val result = mockMvc.post("/api/quotes") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(quote)
        }.andReturn()
        val addedQuote = objectMapper.readValue(result.response.contentAsString, Quote::class.java)
        mockMvc.delete("/api/quotes/${addedQuote.id}").andExpect { status { isNoContent() } }
        mockMvc.delete("/api/quotes/${addedQuote.id}").andExpect { status { isNotFound() } }
    }
}