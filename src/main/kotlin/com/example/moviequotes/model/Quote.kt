package com.example.moviequotes.model

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class Quote(
    val id: Long? = null,
    @field:NotBlank(message = "Quote text is required")
    val text: String,
    @field:NotBlank(message = "Character name is required")
    val character: String,
    @field:NotBlank(message = "Movie title is required")
    val movie: String,
    @field:Min(value = 1900, message = "Year must be 1900 or later")
    val year: Int
)