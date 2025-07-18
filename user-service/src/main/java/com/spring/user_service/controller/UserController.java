package com.spring.user_service.controller;

import com.spring.user_service.dto.request.UserPostRequestDTO;
import com.spring.user_service.dto.request.UserPutRequestDTO;
import com.spring.user_service.dto.response.UserGetResponseDTO;
import com.spring.user_service.dto.response.UserPutResponseDTO;
import com.spring.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Set<UserGetResponseDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<UserGetResponseDTO> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid UserPostRequestDTO userPostRequestDTO) {
        userService.save(userPostRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UserPutResponseDTO> update(@RequestBody @Valid UserPutRequestDTO userPutRequestDTO) {
        return ResponseEntity.ok(userService.update(userPutRequestDTO));
    }

}
