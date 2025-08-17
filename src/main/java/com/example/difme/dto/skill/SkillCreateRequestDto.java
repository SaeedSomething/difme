package com.example.difme.dto.skill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating a new skill")
public class SkillCreateRequestDto {

    @NotBlank(message = "Skill name is required")
    @Schema(description = "Name of the skill", example = "Java Programming")
    private String skillName;

    @NotNull(message = "Skill level is required")
    @Min(value = 1, message = "Skill level must be at least 1")
    @Max(value = 5, message = "Skill level must be at most 5")
    @Schema(description = "Skill proficiency level (1-5)", example = "4")
    private Integer skillLevel;
}
