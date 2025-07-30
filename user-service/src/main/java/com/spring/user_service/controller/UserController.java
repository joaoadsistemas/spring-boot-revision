package com.spring.user_service.controller;

import com.spring.exception.ApiError;
import com.spring.exception.DefaultErrorMessage;
import com.spring.user_service.dto.user.request.UserPostRequestDTO;
import com.spring.user_service.dto.user.request.UserPutRequestDTO;
import com.spring.user_service.dto.user.response.UserGetResponseDTO;
import com.spring.user_service.dto.user.response.UserPutResponseDTO;
import com.spring.user_service.mapper.UserMapper;
import com.spring.user_service.model.User;
import com.spring.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "User related endpoints")
@SecurityRequirement(name = "basicAuth")
public class UserController {

    private final UserService userService;
    private static final UserMapper MAPPER = UserMapper.INSTANCE ;

    @GetMapping
    @Operation(summary = "Get all users", description = "Get all users available in the system",
    responses = {
            @ApiResponse(description = "List all users",
            responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = UserGetResponseDTO.class))))
    })
    public ResponseEntity<Set<UserGetResponseDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll().stream().map(MAPPER::toUserGetResponse).collect(Collectors.toSet()));
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<UserGetResponseDTO>> findAllPaginated(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(userService.findAllPaginated(pageable).map(MAPPER::toUserGetResponse));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find by id",
            responses = {
                    @ApiResponse(description = "Find user by id",
                            responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserGetResponseDTO.class))),
                    @ApiResponse(description = "User not found",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class)))
            })
    public ResponseEntity<UserGetResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(MAPPER.toUserGetResponse(userService.findByIdOrThrowNotFound(id)));
    }

    @GetMapping("/email")
    @Operation(summary = "Find by email",
            responses = {
                    @ApiResponse(description = "Find user by email",
                            responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserGetResponseDTO.class))),
                    @ApiResponse(description = "User not found",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class)))
            })
    public ResponseEntity<UserGetResponseDTO> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok( MAPPER.toUserGetResponse(userService.findByEmailOrThrowNotFound(email)));
    }

    @PostMapping
    @Operation(summary = "Save user",
            responses = {
                    @ApiResponse(description = "Save a user",
                            responseCode = "201",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(description = "Email already exist",
                            responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class))),
                    @ApiResponse(description = "Validation fields Bad Request",
                            responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> save(@RequestBody @Valid UserPostRequestDTO userPostRequestDTO) {
        userService.save(MAPPER.toUser(userPostRequestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user",
            responses = {
                    @ApiResponse(description = "Delete a user",
                            responseCode = "201",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(description = "User not found",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class)))
            })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Operation(summary = "Update user",
            responses = {
                    @ApiResponse(description = "Update a user",
                            responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(description = "Email already exist",
                            responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class))),
                    @ApiResponse(description = "User not found",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class)))
            })
    public ResponseEntity<UserPutResponseDTO> update(@RequestBody @Valid UserPutRequestDTO userPutRequestDTO) {
        User user = MAPPER.toUser(userPutRequestDTO);
        var updatedUser = MAPPER.toUserPutResponse(userService.update(user));
        return ResponseEntity.ok(updatedUser);
    }

}
