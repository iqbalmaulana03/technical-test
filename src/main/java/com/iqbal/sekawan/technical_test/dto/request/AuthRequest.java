package com.iqbal.sekawan.technical_test.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AuthRequest(
        @NotEmpty(message = "email cannot be empty!")
        @Email(message = "invalid email")
        @JsonFormat(pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        String email,

        @NotEmpty(message = "password cannot be empty!")
        @Size(min = 6, message = "password minimum 6 character")
        @JsonFormat(pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")
        String password
) {
}
