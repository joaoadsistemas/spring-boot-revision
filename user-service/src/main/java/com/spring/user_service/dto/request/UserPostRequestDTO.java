package com.spring.user_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserPostRequestDTO(@NotNull(message = "id is required") Long id,
                                 @NotBlank(message = "firstName is required") String firstName,
                                 @NotBlank(message = "lastName is required") String lastName,
                                 @NotBlank(message = "email is required") @Email(message = "email is not valid", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") String email) {
}
