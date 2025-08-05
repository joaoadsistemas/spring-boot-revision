package com.spring.user_service.controller;

import com.spring.api.UserControllerApi;
import com.spring.dto.*;
import com.spring.user_service.mapper.UserMapper;
import com.spring.user_service.model.User;
import com.spring.user_service.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "User related endpoints")
@SecurityRequirement(name = "basicAuth")
public class UserController implements UserControllerApi {

    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserGetResponseDTO>> findAllUsers() {
        return ResponseEntity.ok(userService.findAll().stream().map(mapper::toUserGetResponse).collect(Collectors.toList()));
    }

    @GetMapping("/paginated")
    public ResponseEntity<PageUserGetResponseDTO> findAllUsersPaginated(@ParameterObject Pageable pageable) {
        Page<UserGetResponseDTO> response = userService.findAllPaginated(pageable).map(mapper::toUserGetResponse);
        return ResponseEntity.ok(mapper.toPageUserGetResponseDTO(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponseDTO> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toUserGetResponse(userService.findByIdOrThrowNotFound(id)));
    }

    @GetMapping("/email")
    public ResponseEntity<UserGetResponseDTO> findUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(mapper.toUserGetResponse(userService.findByEmailOrThrowNotFound(email)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> saveUser(@RequestBody @Valid UserPostRequestDTO userPostRequestDTO) {
        userService.save(mapper.toUser(userPostRequestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UserPutResponseDTO> updateUser(@RequestBody @Valid UserPutRequestDTO userPutRequestDTO) {
        User user = mapper.toUser(userPutRequestDTO);
        var updatedUser = mapper.toUserPutResponse(userService.update(user));
        return ResponseEntity.ok(updatedUser);
    }

}
