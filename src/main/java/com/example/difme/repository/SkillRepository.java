package com.example.difme.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.difme.model.SkillModel;

@Repository
public interface SkillRepository extends JpaRepository<SkillModel, Long> {

    Optional<SkillModel> findBySkillName(String skillName);

    @Query("SELECT s FROM SkillModel s WHERE s.skillName LIKE %:name%")
    List<SkillModel> findBySkillNameContaining(@Param("name") String name);
}