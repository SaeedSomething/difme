package com.example.difme.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.difme.model.ProjectModel;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectModel, Long> {

    List<ProjectModel> findByEmployerId(Long employerId);

    @Query("SELECT p FROM ProjectModel p WHERE p.projectName LIKE %:name%")
    List<ProjectModel> findByProjectNameContaining(@Param("name") String name);

    @Query("SELECT p FROM ProjectModel p WHERE p.projectDeadline > :currentDate")
    List<ProjectModel> findActiveProjects(@Param("currentDate") LocalDateTime currentDate);

}