package com.iqbal.sekawan.technical_test.dto.request;

import lombok.Builder;

@Builder
public record SearchContentRequest(
        String title,
        String author,
        String publisher,
        String category,
        String language,
        String tags,
        Integer page,
        Integer size
) {
}