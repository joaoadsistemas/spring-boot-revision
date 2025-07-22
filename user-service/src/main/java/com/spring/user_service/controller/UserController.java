package com.spring.user_service.controller;

import com.spring.user_service.dto.request.UserPostRequestDTO;
import com.spring.user_service.dto.request.UserPutRequestDTO;
import com.spring.user_service.dto.response.UserGetResponseDTO;
import com.spring.user_service.dto.response.UserPutResponseDTO;
import com.spring.user_service.mapper.UserMapper;
import com.spring.user_service.model.User;
import com.spring.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private static final UserMapper MAPPER = UserMapper.INSTANCE ;

    @GetMapping
    public ResponseEntity<Set<UserGetResponseDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll().stream().map(MAPPER::toUserGetResponse).collect(Collectors.toSet()));
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<UserGetResponseDTO>> findAllPaginated(Pageable pageable) {
        return ResponseEntity.ok(userService.findAllPaginated(pageable).map(MAPPER::toUserGetResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(MAPPER.toUserGetResponse(userService.findById(id)));
    }

    @GetMapping("/email")
    public ResponseEntity<UserGetResponseDTO> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok( MAPPER.toUserGetResponse(userService.findByEmail(email)));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid UserPostRequestDTO userPostRequestDTO) {
        userService.save(MAPPER.toUser(userPostRequestDTO));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UserPutResponseDTO> update(@RequestBody @Valid UserPutRequestDTO userPutRequestDTO) {
        User user = MAPPER.toUser(userPutRequestDTO);
        var updatedUser = MAPPER.toUserPutResponse(userService.update(user));
        return ResponseEntity.ok(updatedUser);
    }

}
