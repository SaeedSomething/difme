package com.example.difme.model;

import java.time.LocalDateTime;

import com.example.difme.model.enums.Role;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private LocalDateTime creationDate;
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be in E.164 international format (e.g., +1234567890)")
    private String phoneNumber;

    // Security details for spring securit
    @Builder.Default
    private Role role = Role.USER; // Default role
    @Builder.Default
    private Boolean enabled = true;
    @Builder.Default
    private Boolean accountExpired = false;
    @Builder.Default
    private Boolean accountLocked = false;
    @Builder.Default
    private Boolean credentialsExpired = false;
    // Optional: Track failed login attempts
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    // Optional: Account lockout time

    private LocalDateTime lockedUntil;

    private LocalDateTime lastLogin;

    private LocalDateTime updatedAt;

    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void prePersist() {
        this.creationDate = LocalDateTime.now();
    }

    public boolean isAccountLocked() {
        return lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now());
    }

    public void lockAccount(int minutes) {
        this.lockedUntil = LocalDateTime.now().plusMinutes(minutes);
        this.accountLocked = true;
    }

    public void unlockAccount() {
        this.lockedUntil = null;
        this.accountLocked = false;
        this.failedLoginAttempts = 0;
    }

}
