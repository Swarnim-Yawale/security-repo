package com.example.demo.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateToken_shouldReturnTokenWithCorrectUsername() {
        UserDetails userDetails = User.withUsername("alice")
                .password("secret")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals("alice", jwtService.extractUsername(token));
    }

    @Test
    void isTokenValid_shouldReturnTrue_forValidTokenAndMatchingUser() {
        UserDetails userDetails = User.withUsername("alice")
                .password("secret")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_shouldReturnFalse_forDifferentUsername() {
        UserDetails user = User.withUsername("alice")
                .password("secret")
                .roles("USER")
                .build();

        UserDetails otherUser = User.withUsername("bob")
                .password("secret")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(user);

        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

}