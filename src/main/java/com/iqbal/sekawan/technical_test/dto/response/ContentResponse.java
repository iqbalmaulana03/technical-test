package com.iqbal.sekawan.technical_test.dto.response;

import lombok.Builder;

@Builder
public record ContentResponse(
        Long id,
        String title,
        String description,
        String author,
        String publisher,
        String category,
        String language,
        String keywords,
        String licenseType,
        String status,
        String tags,
        String imageUrl,
        String videoUrl,
        String documentUrl
) {
}
