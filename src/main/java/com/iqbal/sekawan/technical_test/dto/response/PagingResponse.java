package com.iqbal.sekawan.technical_test.dto.response;

import lombok.Builder;

@Builder
public record PagingResponse(
        Integer size,
        Integer page,
        Integer totalPage,
        Long totalElements
) {
}
