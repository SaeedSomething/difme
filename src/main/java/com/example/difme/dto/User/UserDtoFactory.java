package com.example.difme.dto.User;

import lombok.RequiredArgsConstructor;

import com.example.difme.model.UserModel;
import com.example.difme.service.auth.PasswordService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDtoFactory {

    private final PasswordService passwordService;

    public UserResponseDto toResponseDto(UserModel user) {
        return new UserResponseDto(
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreationDate(),
                user.getEmail());
    }

    public UserModel fromCreationRequest(UserCreationRequestDto dto) {
        UserModel user = new UserModel();
        user.setUserName(dto.userName());
        user.setPasswordHash(passwordService.hashPassword(dto.password()));
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        return user;
    }
}