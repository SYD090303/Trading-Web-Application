package com.project.TradingWebApp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.TradingWebApp.domain.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing a user in the trading web application.
 * This class is mapped to a database table and holds user-related details.
 * It includes fields for basic user information, password (handled securely),
 * two-factor authentication settings, and user roles for authorization.
 */
@Entity
@Table(name = "users")  // Ensure it matches the actual DB table name
@Getter
@Setter
public class UserEntity {

    /**
     * Primary key for the UserEntity table.
     * Auto-generated using IDENTITY strategy.
     * `@Id` annotation marks this field as the primary key.
     * `@GeneratedValue(strategy = GenerationType.IDENTITY)` specifies that the primary key values are generated automatically by the database using an identity column.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The full name of the user.
     * `@Column(nullable = false)` annotation specifies that this field cannot be null in the database.
     */
    @Column(nullable = false)
    private String fullName;

    /**
     * The unique username of the user.
     * `@Column(nullable = false, unique = true)` annotation specifies that this field cannot be null and must be unique across all user records in the database.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The email address of the user.
     * `@Column(nullable = false, unique = true)` annotation specifies that this field cannot be null and must be unique across all user records in the database.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The hashed password of the user.
     * This field is marked as WRITE_ONLY to prevent exposure in API responses.
     * `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` annotation from Jackson library ensures that this field is only used during deserialization (e.g., when receiving data in a request) and is not included when serializing the object to JSON (e.g., in API responses).
     * `@Column(nullable = false)` annotation specifies that this field cannot be null in the database.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    /**
     * Embedded two-factor authentication details for the user.
     * By default, a new instance of TwoFactorAuth is created.
     * `@Embedded` annotation indicates that the `TwoFactorAuth` class is embedded into this entity. The fields from `TwoFactorAuth` will be columns in the `UserEntity` table.
     * `twoFactorAuth = new TwoFactorAuth()` initializes the embedded object with default settings, ensuring that every user has 2FA settings initialized, even if 2FA is initially disabled.
     */
    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    /**
     * The role of the user, defining their access level in the system.
     * Default role is set to USER.
     * `@Enumerated(EnumType.STRING)` annotation specifies that the `Role` enum should be stored as a String in the database.
     * `@Column(nullable = false)` annotation specifies that this field cannot be null in the database.
     * `role = Role.USER` sets the default role for a new user to `USER`.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
}