package com.iqbal.sekawan.technical_test.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record ContentRequest(
        @NotEmpty(message = "Title not empty!")
        String title,

        @NotEmpty(message = "Description not empty!")
        String description,

        @NotEmpty(message = "Author not empty!")
        String author,

        @NotEmpty(message = "Publisher not empty!")
        String publisher,

        @NotEmpty(message = "Category not empty!")
        String category,

        @NotEmpty(message = "Language not empty!")
        String language,

        String keywords,

        String licenseType,

        @NotEmpty(message = "Status not empty!")
        String status,

        String tags
) {
}
