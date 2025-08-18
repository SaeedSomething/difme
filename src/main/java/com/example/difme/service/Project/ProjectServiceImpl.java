package com.example.difme.service.Project;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.difme.exception.ResourceNotFoundException;
import com.example.difme.model.ProjectModel;
import com.example.difme.repository.ProjectRepository;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public ProjectModel createProject(ProjectModel project) {
        return projectRepository.save(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectModel> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectModel> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectModel> getProjectsByEmployerId(Long employerId) {
        return projectRepository.findByEmployerId(employerId);
    }

    @Override
    public ProjectModel updateProject(Long id, ProjectModel projectDetails) {
        ProjectModel existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        existingProject.setProjectName(projectDetails.getProjectName());
        existingProject.setProjectDescription(projectDetails.getProjectDescription());
        existingProject.setProjectDeadline(projectDetails.getProjectDeadline());
        existingProject.setProjectStart(projectDetails.getProjectStart());

        return projectRepository.save(existingProject);
    }

    @PreAuthorize("#existingProject.employer.id==@authenticationContext.getCurrentUser().getId() or hasRole('ADMIN')")
    @Override
    public ProjectModel updateProjectDescription(Long id, String description) {
        ProjectModel existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        existingProject.setProjectDescription(description);
        return projectRepository.save(existingProject);
    }

    @Override
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }
}