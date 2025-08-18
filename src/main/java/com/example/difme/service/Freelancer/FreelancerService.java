package com.example.difme.service.Freelancer;

import java.util.List;
import java.util.Optional;

import com.example.difme.model.FreelancerModel;
import com.example.difme.model.SkillModel;

public interface FreelancerService {
    FreelancerModel createFreelancer(FreelancerModel freelancer);

    Optional<FreelancerModel> getFreelancerById(Long id);

    FreelancerModel updateFreelancerSkills(Long freelancerId, List<SkillModel> skills);

    FreelancerModel updateFreelancer(Long id, FreelancerModel freelancerDetails);

    FreelancerModel updateFreelancerProfile(Long id, String firstName, String lastName, String phoneNumber);

    void deleteFreelancer(Long id);
}