package com.iqbal.sekawan.technical_test.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record RegisterUserResponse(
        String email,
        List<String> roles
) {
}
