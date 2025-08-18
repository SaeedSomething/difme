package com.example.difme.dto.employer;

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
@Schema(description = "Employer profile data")
public class EmployerProfileDto extends UserProfileDto {

    public EmployerProfileDto(String userName, String firstName, String lastName,
            String companyDescription) {
        super(userName, firstName, lastName);
        this.companyDescription = companyDescription;
    }

    @Schema(description = "Company description", example = "Leading tech company specializing in web development")
    private String companyDescription;

}