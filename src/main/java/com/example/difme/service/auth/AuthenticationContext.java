package com.example.difme.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.difme.model.UserModel;
import com.example.difme.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class AuthenticationContext {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // System.out.println(authentication);
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal().getClass().equals(User.class)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRepository.findByUserName(userDetails.getUsername()).orElse(null);
        }

        return null;
    }

    public Long getCurrentUserId() {
        // First try to get from JWT token in current request
        String token = getCurrentJwtToken();
        if (token != null) {
            try {
                return jwtService.extractUserId(token);
            } catch (Exception e) {
                // If JWT parsing fails, fallback to database lookup
            }
        }

        // Fallback to database lookup
        UserModel user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    private String getCurrentJwtToken() {
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        } catch (Exception e) {
            // If we can't get request context, return null
        }
        return null;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }
}