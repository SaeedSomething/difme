package com.example.difme.service.Project;

import java.util.List;
import java.util.Optional;

import com.example.difme.model.ProjectModel;

public interface ProjectService {
    ProjectModel createProject(ProjectModel project);

    List<ProjectModel> getAllProjects();

    Optional<ProjectModel> getProjectById(Long id);

    List<ProjectModel> getProjectsByEmployerId(Long employerId);

    ProjectModel updateProject(Long id, ProjectModel projectDetails);

    ProjectModel updateProjectDescription(Long id, String description);

    void deleteProject(Long id);
}