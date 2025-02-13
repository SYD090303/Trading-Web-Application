package com.project.TradingWebApp.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Custom exception for JWT authentication failures.
 * Extends AuthenticationException to integrate with Spring Security.
 */
public class JwtAuthenticationException extends AuthenticationException {

    /**
     * Constructs a JwtAuthenticationException with a message.
     *
     * @param message The error message.
     */
    public JwtAuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a JwtAuthenticationException with a message and a cause.
     *
     * @param message The error message.
     * @param cause   The root cause of the exception.
     */
    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
