package com.spring.user_service.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserPostRequestDTO(
        @NotBlank(message = "firstName is required")
        @Schema(description = "User's first name", example = "Joao")
        String firstName,
        @NotBlank(message = "lastName is required")
        @Schema(description = "User's last name", example = "Silva")
        String lastName,
        @NotBlank(message = "email is required")
        @Email(message = "email is not valid", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        @Schema(description = "User's email. Must be unique", example = "joaosilva@gmail.com")
        String email,
        @NotBlank(message = "password is required")
        @Schema(description = "User's password", example = "mYP4assw0rd*7")
        String password
        ) {
}
