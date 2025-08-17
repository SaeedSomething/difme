package com.example.difme.service.Freelancer;

import java.util.List;
import java.util.Optional;

import com.example.difme.model.SkillModel;

public interface SkillService {
    List<SkillModel> getAllSkills();

    Optional<SkillModel> getSkillById(Long id);

    SkillModel createSkill(SkillModel skill);

    List<SkillModel> getSkillsByIds(List<Long> skillIds);

    SkillModel updateSkill(Long id, SkillModel skillDetails);

    void deleteSkill(Long id);
}