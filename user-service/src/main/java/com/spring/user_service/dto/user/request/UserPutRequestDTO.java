package com.spring.user_service.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserPutRequestDTO(@NotNull(message = "id is required")
                                @Schema(description = "User's id", example = "1")
                                Long id,
                                @NotBlank(message = "firstName is required")
                                @Schema(description = "User's first name", example = "Joao")
                                String firstName,
                                @NotBlank(message = "lastName is required")
                                @Schema(description = "User's last name", example = "Silva")
                                String lastName,
                                @NotBlank(message = "email is required")
                                @Email(message = "email is not valid", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
                                @Schema(description = "User's email. Must be unique", example = "joaosilva@gmail.com")
                                String email) {
}
