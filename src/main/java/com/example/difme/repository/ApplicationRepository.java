package com.example.difme.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.difme.model.ApplicationModel;
import com.example.difme.model.enums.ApplicationStatus;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationModel, Long> {

        List<ApplicationModel> findByProjectId(Long projectId);

        List<ApplicationModel> findByFreelancerId(Long freelancerId);

        List<ApplicationModel> findByStatus(ApplicationStatus status);

        @Query("SELECT a FROM ApplicationModel a WHERE a.project.id = :projectId AND a.status = :status")
        List<ApplicationModel> findByProjectIdAndStatus(@Param("projectId") Long projectId,
                        @Param("status") ApplicationStatus status);

        @Query("SELECT a FROM ApplicationModel a WHERE a.freelancer.id = :freelancerId AND a.status = :status")
        List<ApplicationModel> findByFreelancerIdAndStatus(@Param("freelancerId") Long freelancerId,
                        @Param("status") ApplicationStatus status);

        Optional<ApplicationModel> findByFreelancerIdAndProjectId(Long freelancerId, Long projectId);
}