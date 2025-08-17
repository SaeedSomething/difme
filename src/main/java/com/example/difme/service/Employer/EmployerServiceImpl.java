package com.example.difme.service.Employer;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.difme.exception.ResourceNotFoundException;
import com.example.difme.model.EmployerModel;
import com.example.difme.repository.EmployerRepository;

@Service
@Transactional
public class EmployerServiceImpl implements EmployerService {

    @Autowired
    private EmployerRepository employerRepository;

    @Override
    public EmployerModel createEmployer(EmployerModel employer) {
        
        return employerRepository.save(employer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployerModel> getEmployerById(Long id) {
        return employerRepository.findById(id);
    }

    @Override
    public EmployerModel updateEmployer(Long id, EmployerModel employerDetails) {
        EmployerModel existingEmployer = employerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found with id: " + id));

        existingEmployer.setFirstName(employerDetails.getFirstName());
        existingEmployer.setLastName(employerDetails.getLastName());
        existingEmployer.setEmail(employerDetails.getEmail());
        existingEmployer.setPhoneNumber(employerDetails.getPhoneNumber());
        existingEmployer.setCompanyDescription(employerDetails.getCompanyDescription());
        existingEmployer.preUpdate();

        return employerRepository.save(existingEmployer);
    }

    @Override
    public EmployerModel updateCompanyDescription(Long id, String companyDescription) {
        EmployerModel existingEmployer = employerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found with id: " + id));

        existingEmployer.setCompanyDescription(companyDescription);
        existingEmployer.preUpdate();

        return employerRepository.save(existingEmployer);
    }

    @Override
    @Transactional
    public void deleteEmployer(Long id) {
        if (!employerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employer not found with id: " + id);
        }
        employerRepository.deleteById(id);
    }
}