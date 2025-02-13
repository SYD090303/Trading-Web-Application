package com.project.TradingWebApp.config;

import com.project.TradingWebApp.exceptions.InvalidJwtTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * JwtProvider - Utility class to handle JWT Token generation, validation, and refresh.
 */
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    // Use Secret Key from externalized configuration
    private static final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    // Expiration times (milliseconds)
    private static final long ACCESS_TOKEN_EXPIRATION = 86400000;  // 24 hours
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000; // 7 days

    /**
     * Generates an access token for authenticated users.
     *
     * @param auth The authentication object containing user details.
     * @return The generated JWT access token.
     */
    public static String generateAccessToken(Authentication auth) {
        String roles = populateAuthorities(auth.getAuthorities());

        return Jwts.builder()
                .setSubject(auth.getName())  // Store username/email as subject
                .claim("authorities", roles)  // Store user roles
                .setIssuedAt(new Date())  // Issue time
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))  // Expiry time
                .signWith(key, SignatureAlgorithm.HS512)  // Sign with secret key
                .compact();
    }

    /**
     * Generates a refresh token.
     *
     * @param auth The authentication object.
     * @return The generated JWT refresh token.
     */
    public static String generateRefreshToken(Authentication auth) {
        return Jwts.builder()
                .setSubject(auth.getName())  // Store email
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))  // Expiry in 7 days
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts email (subject) from the token.
     *
     * @param token The JWT token string.
     * @return Extracted email.
     */
    public static String getEmailFromToken(String token) {
        try {
            token = token.startsWith("Bearer ") ? token.substring(7) : token;
            Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (JwtException e) {
            logger.error("Invalid or expired token: " + e.getMessage());
            throw new InvalidJwtTokenException("Invalid or expired token!");
        }
    }

    /**
     * Validates the JWT token.
     *
     * @param token The JWT token.
     * @return True if valid, otherwise false.
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("Token expired: " + e.getMessage());
        } catch (MalformedJwtException | SignatureException | IllegalArgumentException e) {
            logger.error("Invalid token: " + e.getMessage());
        }
        return false;
    }

    /**
     * Converts the user's authorities (roles) into a comma-separated string.
     *
     * @param authorities The collection of user roles.
     * @return A comma-separated string of authorities.
     */
    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority grantedAuthority : authorities) {
            authoritiesSet.add(grantedAuthority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
