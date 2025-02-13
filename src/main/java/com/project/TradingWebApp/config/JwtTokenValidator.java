package com.project.TradingWebApp.config;

import com.project.TradingWebApp.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

/**
 * JwtTokenValidator - A security filter that validates incoming JWT tokens.
 * This filter extracts user details from the token and sets the authentication context.
 */
public class JwtTokenValidator extends OncePerRequestFilter {

    /**
     * Intercepts each request and validates the JWT token.
     *
     * @param request     The incoming HTTP request.
     * @param response    The outgoing HTTP response.
     * @param filterChain The filter chain to continue processing.
     * @throws ServletException If an error occurs during request processing.
     * @throws IOException      If an input/output error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve Authorization header (JWT token)
        String authHeader = request.getHeader(JwtConstant.JWT_HEADER);

        // Check if the Authorization header is present and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer " prefix

            try {
                // Generate secret key for JWT validation
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

                // Parse JWT token and extract claims
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // Extract email from the token claims (using standard 'sub' claim)
                String email = claims.getSubject();

                // Extract authorities (roles) from token claims
                String authorities = claims.get("authorities", String.class);

                // Convert comma-separated roles into a List of GrantedAuthority
                List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // Create authentication object with user details
                Authentication auth = new UsernamePasswordAuthenticationToken(email, null, authorityList);

                // Set authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (ExpiredJwtException e) {
                throw new JwtAuthenticationException("JWT token has expired", e);
            } catch (MalformedJwtException e) {
                throw new JwtAuthenticationException("Invalid JWT token format", e);
            } catch (Exception e) {
                throw new JwtAuthenticationException("Failed to process JWT token", e);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
