package com.example.difme.service.Application;

import java.util.List;
import java.util.Optional;

import com.example.difme.model.ApplicationModel;
import com.example.difme.model.enums.ApplicationStatus;

public interface ApplicationService {
    ApplicationModel createApplication(ApplicationModel application);

    List<ApplicationModel> getApplicationsByProjectId(Long projectId);

    List<ApplicationModel> getApplicationsByFreelancerId(Long freelancerId);

    Optional<ApplicationModel> getApplicationById(Long id);

    ApplicationModel updateApplicationStatus(Long applicationId, ApplicationStatus status);

    ApplicationModel updateApplication(Long id, ApplicationModel applicationDetails);

    ApplicationModel updateCoverLetter(Long id, String coverLetter);

    void deleteApplication(Long id);
}