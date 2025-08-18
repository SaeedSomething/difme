package com.example.difme.dto.freelancer;

import java.util.List;

import com.example.difme.dto.User.UserProfileDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Freelancer profile data")
public class FreelancerProfileDto extends UserProfileDto {

    public FreelancerProfileDto(String userName, String firstName, String lastName,
            List<String> skills) {
        super(userName, firstName, lastName);
        this.skills = skills;
    }

    @Schema(description = "List of skills", example = "[\"Java\", \"Spring\", \"SQL\"]")
    private List<String> skills;

}