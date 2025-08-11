package com.example.moviequotes.service

import com.example.moviequotes.model.Quote
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import kotlin.random.Random
import java.time.LocalDate

@Service
class QuoteService {
    private val quotes = mutableListOf<Quote>()
    private val idGenerator = AtomicLong(1)

    fun addQuote(quote: Quote): Quote {
        val newQuote = quote.copy(id = idGenerator.getAndIncrement())
        quotes.add(newQuote)
        return newQuote
    }

    fun getAllQuotes(): List<Quote> = quotes.toList()

    fun getQuoteById(id: Long): Quote? = quotes.find { it.id == id }

    fun deleteQuote(id: Long): Boolean {
        val quote = getQuoteById(id)
        return if (quote != null) {
            quotes.remove(quote)
            true
        } else false
    }

    fun searchQuotes(character: String? = null, movie: String? = null): List<Quote> {
        return quotes.filter {
            (character == null || it.character.lowercase().contains(character.lowercase())) &&
            (movie == null || it.movie.lowercase().contains(movie.lowercase()))
        }
    }

    fun getQuoteOfTheDay(): Quote? {
        if (quotes.isEmpty()) return null
        val today = LocalDate.now()
        val seed = today.year * 10000L + today.monthValue * 100L + today.dayOfMonth
        val random = Random(seed)
        val index = random.nextInt(quotes.size)
        return quotes[index]
    }

    // Add this for test isolation
    fun reset() {
        quotes.clear()
        idGenerator.set(1)
    }
}