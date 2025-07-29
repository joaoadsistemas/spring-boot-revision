package com.spring.user_service.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserPostResponseDTO(
        @Schema(description = "User's id", example = "1")
        Long id,
        @Schema(description = "User's first name", example = "Joao")
        String firstName,
        @Schema(description = "User's last name", example = "Silva")
        String lastName,
        @Schema(description = "User's email", example = "joaosilva@gmail.com")
        String email) {
}
