package com.example.difme.service;

import com.example.difme.dto.User.ChangePasswordRequestDto;
import com.example.difme.dto.User.ChangePasswordResponseDto;
import com.example.difme.dto.User.UserCreationRequestDto;
import com.example.difme.dto.User.UserResponseDto;
import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserCreationRequestDto Dto);

    UserResponseDto getUserByUsername(String username);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    ChangePasswordResponseDto changePassword(Long userId, ChangePasswordRequestDto request);

    UserResponseDto updateUser(Long id, UserCreationRequestDto Dto);

    void deleteUser(Long id);
}
