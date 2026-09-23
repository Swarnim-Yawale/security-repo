package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService);
    }

    @Test
    void signupShouldReturnCreatedStatus() {
        SignupRequest request = new SignupRequest();
        request.setUsername("alice");
        request.setPassword("secret");

        when(authService.signUp(request)).thenReturn("User registered successfully");

        ResponseEntity<Map<String, String>> response = authController.signup(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody().get("message"));
    }

    @Test
    void loginShouldReturnToken() {
        LoginRequest request = new LoginRequest();
        request.setUsername("alice");
        request.setPassword("secret");

        when(authService.login(request)).thenReturn("jwt-token");

        ResponseEntity<Map<String, String>> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody().get("token"));
    }

    @Test
    void profileShouldReturnUsernameAndMessage() {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("alice");

        ResponseEntity<Map<String, String>> response = authController.profile(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("alice", response.getBody().get("username"));
        assertEquals("Protected API accessed", response.getBody().get("message"));
    }
}