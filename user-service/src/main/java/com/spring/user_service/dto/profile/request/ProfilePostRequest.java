package com.spring.user_service.dto.profile.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfilePostRequest(
        @NotBlank(message = "name can not be blank") @NotNull(message = "name can not be null") String name,
        @NotBlank(message = "description can not be blank") @NotNull(message = "description can not be null") String description) {
}
