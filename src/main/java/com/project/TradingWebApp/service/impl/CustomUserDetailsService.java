package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the UserDetailsService interface for custom user authentication.
 * This class loads user details from the database based on the provided username (email).
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Loads user details by username (email).
     *
     * @param username The username (email) of the user to load.
     * @return UserDetails object containing the loaded user details.
     * @throws UsernameNotFoundException If the user with the given username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with email '" + username + "' not found."); // More informative message
        }

        // Granted Authorities (Roles/Permissions) -  Currently empty.  Needs implementation.
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(); // Initialize the list

        // Create and return UserDetails object.  Note: Authorities are currently empty!
        return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}