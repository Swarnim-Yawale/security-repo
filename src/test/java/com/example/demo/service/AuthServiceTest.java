package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository,
                passwordEncoder,
                authenticationManager,
                userDetailsService,
                jwtService
        );
    }

    @Test
    void signUpShouldRegisterUserWhenUsernameDoesNotExist() {
        SignupRequest request = new SignupRequest();
        request.setUsername("alice");
        request.setPassword("secret");

        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        String message = authService.signUp(request);

        assertEquals("User registered successfully", message);
    }

    @Test
    void signUpShouldThrowWhenUsernameAlreadyExists() {
        SignupRequest request = new SignupRequest();
        request.setUsername("alice");
        request.setPassword("secret");

        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.signUp(request));
    }

    @Test
    void loginShouldAuthenticateAndReturnJwtToken() {
        LoginRequest request = new LoginRequest();
        request.setUsername("alice");
        request.setPassword("secret");

        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("alice")
                .password("encoded-secret")
                .roles("USER")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userDetailsService.loadUserByUsername("alice")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("jwt-token");

        String token = authService.login(request);

        assertEquals("jwt-token", token);
    }
}