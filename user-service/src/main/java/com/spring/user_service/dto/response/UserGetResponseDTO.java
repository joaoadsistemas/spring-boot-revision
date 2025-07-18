package com.spring.user_service.dto.response;

public record UserGetResponseDTO(Long id, String firstName, String lastName, String email) {
}
