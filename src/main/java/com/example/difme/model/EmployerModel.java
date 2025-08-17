package com.example.difme.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "employers")
public class EmployerModel extends UserModel {

    public EmployerModel() {
        super();
    }

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProjectModel> projects;

    @Column(name = "company_description", nullable = true, columnDefinition = "TEXT")
    private String companyDescription;

}
