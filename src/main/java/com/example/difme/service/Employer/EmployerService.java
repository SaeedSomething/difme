package com.example.difme.service.Employer;

import java.util.Optional;

import com.example.difme.model.EmployerModel;

public interface EmployerService {
    EmployerModel createEmployer(EmployerModel employer);

    Optional<EmployerModel> getEmployerById(Long id);

    EmployerModel updateEmployer(Long id, EmployerModel employerDetails);
    EmployerModel updateCompanyDescription(Long id, String companyDescription);
    void deleteEmployer(Long id);
}