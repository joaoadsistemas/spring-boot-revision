package com.spring.user_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserPutRequestDTO(@NotNull(message = "id is required") Long id,
                                @NotBlank(message = "firstName is required") String firstName,
                                @NotBlank(message = "lastName is required") String lastName,
                                @Email(message = "email is required", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") String email) {
}
