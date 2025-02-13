package com.project.TradingWebApp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.TradingWebApp.domain.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing a user in the trading web application.
 * This class is mapped to a database table and holds user-related details.
 */
@Entity
@Table(name = "users")  // Ensure it matches the actual DB table name
@Getter
@Setter
public class UserEntity {

    /**
     * Primary key for the UserEntity table.
     * Auto-generated using IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The full name of the user.
     */
    @Column(nullable = false)
    private String fullName;

    /**
     * The unique username of the user.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The email address of the user.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The hashed password of the user.
     * This field is marked as WRITE_ONLY to prevent exposure in API responses.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    /**
     * Embedded two-factor authentication details for the user.
     * By default, a new instance of TwoFactorAuth is created.
     */
    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    /**
     * The role of the user, defining their access level in the system.
     * Default role is set to USER.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
}
