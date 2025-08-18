package com.example.difme.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.difme.model.EmployerModel;

@Repository
public interface EmployerRepository extends JpaRepository<EmployerModel, Long> {

    Optional<EmployerModel> findByEmail(String email);

    Optional<EmployerModel> findByUserName(String userName);

    @Query("SELECT e FROM EmployerModel e WHERE e.firstName LIKE %:name% OR e.lastName LIKE %:name%")
    List<EmployerModel> findByNameContaining(@Param("name") String name);
}