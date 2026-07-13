package com.kailandias.bookapi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookRequest(
        @NotBlank(message = "title is required")
        String title,

        @NotBlank(message = "author is required")
        String author,

        @NotNull(message = "yearPublication is required")
        @Positive(message = "yearPublication must be positive")
        Integer yearPublication
) {}