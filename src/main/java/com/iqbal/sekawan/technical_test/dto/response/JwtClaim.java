package com.iqbal.sekawan.technical_test.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record JwtClaim(
        String userId,
        List<String> roles
) {

}
