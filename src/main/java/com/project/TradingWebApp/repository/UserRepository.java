package com.project.TradingWebApp.repository;

import com.project.TradingWebApp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing UserEntity persistence.
 * Extends JpaRepository to provide CRUD operations and query execution.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email of the user.
     * @return The UserEntity object if found, otherwise null.
     */
    UserEntity findByEmail(String email);
}
