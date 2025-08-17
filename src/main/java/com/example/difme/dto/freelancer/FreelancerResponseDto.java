package com.example.difme.dto.freelancer;

import java.time.LocalDateTime;
import java.util.List;

import com.example.difme.dto.skill.SkillResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Freelancer response data")
public class FreelancerResponseDto {

    @Schema(description = "Freelancer ID", example = "1")
    private Long id;

    @Schema(description = "Username", example = "john_developer")
    private String userName;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Email address", example = "john.doe@email.com")
    private String email;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "List of freelancer's skills")
    private List<SkillResponseDto> skills;

}
