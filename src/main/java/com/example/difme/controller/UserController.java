package com.example.difme.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import com.example.difme.dto.User.UserCreationRequestDto;
import com.example.difme.dto.User.UserLoginDto;
import com.example.difme.dto.User.UserLoginResponseDto;
import com.example.difme.dto.User.UserResponseDto;
import com.example.difme.service.UserService;
import com.example.difme.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@EnableMethodSecurity(prePostEnabled = true)
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
public class UserController {

        private final UserService userService;
        private final AuthService authServiceImpl;

        @Operation(summary = "Get a user by username", description = "Returns a user based on their username")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @GetMapping("/{userName}")
        public ResponseEntity<UserResponseDto> getUserByUsername(
                        @Parameter(description = "Username of the user", required = true) @PathVariable String userName) {
                UserResponseDto respDto = userService.getUserByUsername(userName);
                return ResponseEntity.ok().body(respDto);
        }

        @Operation(summary = "Get a user by ID", description = "Returns a user based on their ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @GetMapping("/id/{id}")
        public ResponseEntity<UserResponseDto> getUserById(
                        @Parameter(description = "ID of the user", required = true) @PathVariable Long id) {
                UserResponseDto respDto = userService.getUserById(id);
                return ResponseEntity.ok().body(respDto);
        }

        @Operation(summary = "Get all users", description = "Returns a list of all users")
        @ApiResponse(responseCode = "200", description = "List of users retrieved", content = @Content(schema = @Schema(implementation = UserResponseDto.class)))
        @GetMapping
        public ResponseEntity<List<UserResponseDto>> getAllUsers() {
                List<UserResponseDto> users = userService.getAllUsers();
                return ResponseEntity.ok().body(users);
        }

        @Operation(summary = "Update an existing user", description = "Updates a user's information and returns the updated user details")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data")
        })
        @PutMapping("/{id}")
        public ResponseEntity<UserResponseDto> updateUser(
                        @Parameter(description = "ID of the user to update", required = true) @PathVariable Long id,
                        @Parameter(description = "Updated user data", required = true) @RequestBody UserCreationRequestDto reqDto) {
                UserResponseDto respDto = userService.updateUser(id, reqDto);
                return ResponseEntity.ok().body(respDto);
        }

        @Operation(summary = "Delete a user", description = "Deletes a user by their ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteUser(
                        @Parameter(description = "ID of the user to delete", required = true) @PathVariable Long id) {
                userService.deleteUser(id);
                return ResponseEntity.noContent().build();
        }

}
