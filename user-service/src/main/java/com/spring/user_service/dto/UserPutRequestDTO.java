package com.spring.user_service.dto;

public record UserPutRequestDTO(Long id, String firstName, String lastName, String email) {
}
