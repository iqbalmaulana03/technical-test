package com.iqbal.sekawan.technical_test.dto.response;

import lombok.Builder;

@Builder
public record WebResponse<T>(
        String message,
        String status,
        T data,
        PagingResponse paging
) {
}
