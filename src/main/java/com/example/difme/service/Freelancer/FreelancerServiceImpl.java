package com.example.difme.service.Freelancer;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.difme.exception.ResourceNotFoundException;
import com.example.difme.model.FreelancerModel;
import com.example.difme.model.SkillModel;
import com.example.difme.repository.FreelancerRepository;

@Service
@Transactional
public class FreelancerServiceImpl implements FreelancerService {

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Override
    public FreelancerModel createFreelancer(FreelancerModel freelancer) {
        return freelancerRepository.save(freelancer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FreelancerModel> getFreelancerById(Long id) {
        return freelancerRepository.findById(id);
    }

    @Override
    public FreelancerModel updateFreelancerSkills(Long freelancerId, List<SkillModel> skills) {
        FreelancerModel freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer not found with id: " + freelancerId));

        freelancer.setSkills(new HashSet<>(skills));
        return freelancerRepository.save(freelancer);
    }

    @Override
    public FreelancerModel updateFreelancer(Long id, FreelancerModel freelancerDetails) {
        FreelancerModel existingFreelancer = freelancerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer not found with id: " + id));

        existingFreelancer.setFirstName(freelancerDetails.getFirstName());
        existingFreelancer.setLastName(freelancerDetails.getLastName());
        existingFreelancer.setEmail(freelancerDetails.getEmail());
        existingFreelancer.setPhoneNumber(freelancerDetails.getPhoneNumber());
        existingFreelancer.preUpdate();

        return freelancerRepository.save(existingFreelancer);
    }

    @Override
    public FreelancerModel updateFreelancerProfile(Long id, String firstName, String lastName, String phoneNumber) {
        FreelancerModel existingFreelancer = freelancerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer not found with id: " + id));

        existingFreelancer.setFirstName(firstName);
        existingFreelancer.setLastName(lastName);
        existingFreelancer.setPhoneNumber(phoneNumber);
        existingFreelancer.preUpdate();

        return freelancerRepository.save(existingFreelancer);
    }

    @Override
    public void deleteFreelancer(Long id) {
        if (!freelancerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Freelancer not found with id: " + id);
        }
        freelancerRepository.deleteById(id);
    }
}