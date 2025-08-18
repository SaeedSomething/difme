package com.example.difme.service.User;

import com.example.difme.dto.User.ChangePasswordRequestDto;
import com.example.difme.dto.User.ChangePasswordResponseDto;
import com.example.difme.dto.User.UserCreationRequestDto;
import com.example.difme.dto.User.UserDtoFactory;
import com.example.difme.dto.User.UserResponseDto;
import com.example.difme.exception.ApiException;
import com.example.difme.exception.BadRequestException;
import com.example.difme.exception.ResourceNotFoundException;
import com.example.difme.model.UserModel;
import com.example.difme.repository.UserRepository;
import com.example.difme.service.auth.PasswordService;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    private final UserDtoFactory userDtoFactory;

    @Override
    // @Transactional
    public UserResponseDto createUser(UserCreationRequestDto Dto) {
        UserModel user = userDtoFactory.fromCreationRequest(Dto);
        user = userRepository.save(user);
        return userDtoFactory.toResponseDto(user);
    }

    @Override
    public UserResponseDto getUserByUsername(String userName) {
        UserModel user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found with this username", 404));
        return userDtoFactory.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto getUserById(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found with id: " + id, 404));
        return userDtoFactory.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userDtoFactory::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserCreationRequestDto Dto) {
        UserModel existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found with id: " + id, 404));

        // Update the user fields
        existingUser.setUserName(Dto.userName());
        existingUser.setFirstName(Dto.firstName());
        existingUser.setLastName(Dto.lastName());
        existingUser.setEmail(Dto.email());
        // Don't update password hash unless it's provided and different
        // TODO : add a diff rout to handle password change
        if (Dto.password() != null && !Dto.password().isEmpty()) {
            existingUser.setPasswordHash(Dto.password());
        }

        // Save and return the updated user
        UserModel updatedUser = userRepository.save(existingUser);
        return userDtoFactory.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "User not found with id: " + id, 404);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ChangePasswordResponseDto changePassword(Long userId, ChangePasswordRequestDto request) {
        log.debug("User {} attempting to change password", userId);

        // 1. Validate password confirmation
        if (!request.isPasswordConfirmed()) {
            throw new BadRequestException("Password confirmation does not match new password");
        }

        // 2. Get user
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // 3. Verify current password
        if (!passwordService.verifyPassword(request.currentPassword(), user.getPasswordHash())) {
            log.warn("User {} provided incorrect current password", userId);
            throw new BadCredentialsException("Current password is incorrect");
        }

        // 4. Check if new password is different from current
        if (passwordService.verifyPassword(request.newPassword(), user.getPasswordHash())) {
            throw new BadRequestException("New password must be different from current password");
        }

        // 5. Hash and update new password
        String hashedNewPassword = passwordService.hashPassword(request.newPassword());
        user.setPasswordHash(hashedNewPassword);

        // Update timestamp if you have one
        // user.setPasswordChangedAt(LocalDateTime.now());

        userRepository.save(user);

        log.info("Password successfully changed for user {}", userId);

        return ChangePasswordResponseDto.builder()
                .message("Password changed successfully")
                .changedAt(LocalDateTime.now())
                .requiresReAuthentication(false) // JWT stays valid unless you want to invalidate it
                .build();
    }
}