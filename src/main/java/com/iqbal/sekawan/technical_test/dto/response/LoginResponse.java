package com.iqbal.sekawan.technical_test.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record LoginResponse(
        String token,
        List<String> parts
) {
}
