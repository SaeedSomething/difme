package com.example.difme.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.difme.model.FreelancerModel;

@Repository
public interface FreelancerRepository extends JpaRepository<FreelancerModel, Long> {

    Optional<FreelancerModel> findByEmail(String email);

    Optional<FreelancerModel> findByUserName(String userName);

    @Query("SELECT f FROM FreelancerModel f WHERE f.firstName LIKE %:name% OR f.lastName LIKE %:name%")
    List<FreelancerModel> findByNameContaining(@Param("name") String name);

    @Query("SELECT f FROM FreelancerModel f JOIN f.skills s WHERE s.id IN :skillIds")
    List<FreelancerModel> findBySkillIds(@Param("skillIds") List<Long> skillIds);

}