package com.example.difme.model;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projects")
public class ProjectModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ApplicationModel> applications;
    @Column(name = "project_description", nullable = false, columnDefinition = "TEXT")
    private String projectDescription;

    @Column(name = "project_deadline", nullable = false)
    private LocalDateTime projectDeadline;
    @Column(name = "project_start", nullable = false)
    private LocalDateTime projectStart;
    @ManyToOne
    @JoinColumn(name = "employer_id")
    private EmployerModel employer;
    // TODO: Implement project status , project applicant limit, skills required
}
