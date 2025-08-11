package com.example.moviequotes.controller

import com.example.moviequotes.model.Quote
import com.example.moviequotes.service.QuoteService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/quotes")
class QuoteController(private val quoteService: QuoteService) {

    @PostMapping
    fun addQuote(@Valid @RequestBody quote: Quote): ResponseEntity<Quote> {
        val newQuote = quoteService.addQuote(quote)
        return ResponseEntity(newQuote, HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllQuotes(): List<Quote> = quoteService.getAllQuotes()

    @GetMapping("/{id}")
    fun getQuoteById(@PathVariable id: Long): ResponseEntity<Quote> {
        val quote = quoteService.getQuoteById(id)
        return if (quote != null) ResponseEntity.ok(quote)
        else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteQuote(@PathVariable id: Long): ResponseEntity<Void> {
        val deleted = quoteService.deleteQuote(id)
        return if (deleted) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }

    @GetMapping("/search")
    fun searchQuotes(
        @RequestParam(required = false) character: String?,
        @RequestParam(required = false) movie: String?
    ): List<Quote> {
        return quoteService.searchQuotes(character, movie)
    }

    @GetMapping("/daily")
    fun getQuoteOfTheDay(): ResponseEntity<Quote> {
        val quote = quoteService.getQuoteOfTheDay()
        return if (quote != null) ResponseEntity.ok(quote)
        else ResponseEntity.noContent().build()
    }
}