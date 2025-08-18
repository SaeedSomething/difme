package com.example.difme.controller;

import com.example.difme.dto.User.ChangePasswordRequestDto;
import com.example.difme.dto.User.ChangePasswordResponseDto;
import com.example.difme.dto.User.UserCreationRequestDto;
import com.example.difme.dto.User.UserLoginDto;
import com.example.difme.dto.User.UserLoginResponseDto;
import com.example.difme.dto.User.UserProfileDto;
import com.example.difme.dto.User.UserResponseDto;
import com.example.difme.dto.employer.EmployerCreateRequestDto;
import com.example.difme.dto.employer.EmployerProfileDto;
import com.example.difme.dto.employer.EmployerResponseDto;
import com.example.difme.dto.factory.EmployerDtoFactory;
import com.example.difme.dto.factory.FreelancerDtoFactory;
import com.example.difme.dto.freelancer.FreelancerCreateRequestDto;
import com.example.difme.dto.freelancer.FreelancerProfileDto;
import com.example.difme.dto.freelancer.FreelancerResponseDto;
import com.example.difme.model.EmployerModel;
import com.example.difme.model.FreelancerModel;
import com.example.difme.model.UserModel;
import com.example.difme.service.Employer.EmployerService;
import com.example.difme.service.Freelancer.FreelancerService;
import com.example.difme.service.User.UserService;
import com.example.difme.service.auth.AuthService;
import com.example.difme.service.auth.AuthenticationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.difme.dto.MyApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@EnableMethodSecurity(prePostEnabled = true)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
        private final UserService userService;
        private final AuthService authService;
        private final AuthenticationContext authenticationContext;
        private final EmployerDtoFactory employerDtoFactory;
        private final EmployerService employerService;
        private final FreelancerDtoFactory freelancerDtoFactory;
        private final FreelancerService freelancerService;

        @Operation(summary = "Create a new user", description = "Creates a new user and returns the created user details")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data")
        })
        @PostMapping("/signup")
        @Deprecated
        public ResponseEntity<UserResponseDto> createUser(
                        @Parameter(description = "User data to create", required = true) @RequestBody UserCreationRequestDto reqDto) {
                UserResponseDto respDto = userService.createUser(reqDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(respDto);
        }

        @PostMapping("/login")
        public ResponseEntity<UserLoginResponseDto> UserLoginDto(@RequestBody UserLoginDto userLoginDto) {
                String token = authService.login(userLoginDto.userName(), userLoginDto.password());
                if (token == null)
                        return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
                return ResponseEntity.ok(new UserLoginResponseDto(token));
        }

        @GetMapping("/me")
        public ResponseEntity<UserModel> getCurrentUser() {
                UserModel user = authenticationContext.getCurrentUser();
                if (user == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                return ResponseEntity.ok(user);// ! dev only , didnt want ot inject dto factory and model should not be
                                               // sent to user
        }

        @Operation(summary = "Get user profile", description = "Get the current user's profile information. Returns different profile data based on user type (Freelancer or Employer).")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully", content = @Content(schema = @Schema(oneOf = {
                                        FreelancerProfileDto.class, EmployerProfileDto.class, UserProfileDto.class }))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or expired token"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @SecurityRequirement(name = "bearerAuth")
        @GetMapping("/profile")
        public ResponseEntity<MyApiResponse<UserProfileDto>> getUserProfile() {
                UserModel user = authenticationContext.getCurrentUser();
                if (user == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(MyApiResponse.error("User not found", null));
                }

                UserProfileDto profileDto;
                String message;

                // Check user type and create appropriate profile DTO
                if (user instanceof FreelancerModel) {
                        FreelancerModel freelancer = (FreelancerModel) user;
                        profileDto = freelancerDtoFactory.toProfileDto(freelancer);
                        message = "Freelancer profile retrieved successfully";
                } else if (user instanceof EmployerModel) {
                        EmployerModel employer = (EmployerModel) user;
                        profileDto = employerDtoFactory.toProfileDto(employer);
                        message = "Employer profile retrieved successfully";
                } else {
                        // Fallback for base UserModel (if needed)
                        profileDto = new UserProfileDto(user.getUserName(), user.getFirstName(), user.getLastName());
                        message = "User profile retrieved successfully";
                }

                return ResponseEntity.ok(MyApiResponse.success(profileDto, message));
        }

        @Operation(summary = "Change password", description = "Change the current user's password. Requires authentication.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
                        @ApiResponse(responseCode = "400", description = "Bad request - Invalid current password or password validation failed"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or expired token"),
                        @ApiResponse(responseCode = "422", description = "Unprocessable Entity - Password requirements not met")
        })
        @SecurityRequirement(name = "bearerAuth")
        @PutMapping("/change-password")
        public ResponseEntity<MyApiResponse<ChangePasswordResponseDto>> changePassword(
                        @Parameter(description = "Password change request") @Valid @RequestBody ChangePasswordRequestDto request) {

                Long userId = authenticationContext.getCurrentUserId();

                ChangePasswordResponseDto response = userService.changePassword(userId, request);

                return ResponseEntity.ok(MyApiResponse.success(
                                response,
                                "Password changed successfully"));
        }

        @PostMapping("/employers")
        @Operation(summary = "Sign up as employer", description = "Register a new employer in the system")
        @ApiResponse(responseCode = "201", description = "Employer created successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
        @ApiResponse(responseCode = "400", description = "Invalid employer data")
        public ResponseEntity<MyApiResponse<EmployerResponseDto>> signUpAsEmployer(
                        @Parameter(description = "Employer details") @Valid @RequestBody EmployerCreateRequestDto employerDto) {
                EmployerModel employer = employerDtoFactory.toModel(employerDto);
                EmployerModel createdEmployer = employerService.createEmployer(employer);
                EmployerResponseDto responseDto = employerDtoFactory.toResponseDto(createdEmployer);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(MyApiResponse.success(responseDto, "Employer created successfully"));
        }

        @PostMapping("/freelancers")
        @Operation(summary = "Sign up as freelancer", description = "Register a new freelancer in the system")
        @ApiResponse(responseCode = "201", description = "Freelancer created successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
        @ApiResponse(responseCode = "400", description = "Invalid freelancer data")
        public ResponseEntity<MyApiResponse<FreelancerResponseDto>> createFreelancer(
                        @Parameter(description = "Freelancer details") @Valid @RequestBody FreelancerCreateRequestDto freelancerDto) {
                FreelancerModel freelancer = freelancerDtoFactory.toModel(freelancerDto);
                FreelancerModel createdFreelancer = freelancerService.createFreelancer(freelancer);
                FreelancerResponseDto responseDto = freelancerDtoFactory.toResponseDto(createdFreelancer);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(MyApiResponse.success(responseDto, "Freelancer created successfully"));
        }
}
