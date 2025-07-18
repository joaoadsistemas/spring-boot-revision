package com.spring.user_service.service;

import com.spring.user_service.dto.response.UserGetResponseDTO;
import com.spring.user_service.dto.request.UserPostRequestDTO;
import com.spring.user_service.dto.request.UserPutRequestDTO;
import com.spring.user_service.dto.response.UserPutResponseDTO;
import com.spring.user_service.mapper.UserMapper;
import com.spring.user_service.model.User;
import com.spring.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final UserMapper MAPPER = UserMapper.INSTANCE;

    public Set<UserGetResponseDTO> findAll() {
        return userRepository.findAll().stream().map(MAPPER::toUserGetResponse).collect(Collectors.toSet());

    }

    public UserGetResponseDTO findById(Long id) {
        return MAPPER.toUserGetResponse(userRepository.findById(id));
    }

    public UserGetResponseDTO findByEmail(String email) {
        return MAPPER.toUserGetResponse(userRepository.findByEmail(email));
    }

    public void save(UserPostRequestDTO userPostRequestDTO) {
        User user = MAPPER.toUser(userPostRequestDTO);
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.delete(id);
    }

    public UserPutResponseDTO update(UserPutRequestDTO userPutRequestDTO) {
        User user = MAPPER.toUser(userPutRequestDTO);
        return MAPPER.toUserPutResponse(userRepository.update(user));
    }

}
