package com.example.difme.dto.skill;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Skill response data")
public class SkillResponseDto {

    @Schema(description = "Skill ID", example = "1")
    private Long id;

    @Schema(description = "Name of the skill", example = "Java Programming")
    private String skillName;

    @Schema(description = "Skill proficiency level (1-5)", example = "4")
    private Integer skillLevel;
}
