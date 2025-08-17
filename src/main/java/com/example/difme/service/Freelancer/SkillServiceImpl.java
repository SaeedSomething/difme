package com.example.difme.service.Freelancer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.difme.exception.ResourceNotFoundException;
import com.example.difme.model.SkillModel;
import com.example.difme.repository.SkillRepository;

@Service
@Transactional
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SkillModel> getAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SkillModel> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    @Override
    public SkillModel createSkill(SkillModel skill) {
        return skillRepository.save(skill);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillModel> getSkillsByIds(List<Long> skillIds) {
        return skillRepository.findAllById(skillIds);
    }

    @Override
    @Transactional
    public SkillModel updateSkill(Long id, SkillModel skillDetails) {
        SkillModel existingSkill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));

        existingSkill.setSkillName(skillDetails.getSkillName());
        existingSkill.setSkillLevel(skillDetails.getSkillLevel());

        return skillRepository.save(existingSkill);
    }

    @Override
    @Transactional
    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new ResourceNotFoundException("Skill not found with id: " + id);
        }
        skillRepository.deleteById(id);
    }
}