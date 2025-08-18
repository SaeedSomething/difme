package com.example.difme.service.Application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.difme.exception.ResourceNotFoundException;
import com.example.difme.model.ApplicationModel;
import com.example.difme.model.enums.ApplicationStatus;
import com.example.difme.repository.ApplicationRepository;
import com.example.difme.service.auth.AuthenticationContext;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final AuthenticationContext authenticationContext;
    private final ApplicationRepository applicationRepository;

    @Override
    public ApplicationModel createApplication(ApplicationModel application) {
        return applicationRepository.save(application);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationModel> getApplicationsByProjectId(Long projectId) {
        return applicationRepository.findByProjectId(projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationModel> getApplicationsByFreelancerId(Long freelancerId) {
        return applicationRepository.findByFreelancerId(freelancerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationModel> getApplicationById(Long id) {
        ApplicationModel applicationModel = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no such application exists"));
        if (authenticationContext.getCurrentUserId() == applicationModel.getFreelancer().getId()
                || authenticationContext.getCurrentUserId() == applicationModel.getFreelancer().getId() ||
                authenticationContext.getCurrentUser().getRole().equals("ADMIN")) {
            return Optional.of(applicationModel);
        }
        return Optional.empty();
    }

    @Override
    public ApplicationModel updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        ApplicationModel application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());
        return applicationRepository.save(application);
    }

    @Override
    public ApplicationModel updateApplication(Long id, ApplicationModel applicationDetails) {
        ApplicationModel existingApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        existingApplication.setCoverLetter(applicationDetails.getCoverLetter());
        existingApplication.setStatus(applicationDetails.getStatus());
        existingApplication.setUpdatedAt(LocalDateTime.now());

        return applicationRepository.save(existingApplication);
    }

    @Override
    public ApplicationModel updateCoverLetter(Long id, String coverLetter) {
        ApplicationModel existingApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        existingApplication.setCoverLetter(coverLetter);
        existingApplication.setUpdatedAt(LocalDateTime.now());

        return applicationRepository.save(existingApplication);
    }

    @Override
    public void deleteApplication(Long id) {
        if (!applicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Application not found with id: " + id);
        }
        applicationRepository.deleteById(id);
    }
}