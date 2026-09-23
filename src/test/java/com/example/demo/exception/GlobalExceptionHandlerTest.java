package com.example.demo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleUserAlreadyExistsShouldReturnConflictResponse() {
        UserAlreadyExistsException ex =
                new UserAlreadyExistsException("Username already exists: alice");

        ResponseEntity<Map<String, Object>> response = handler.handleUserAlreadyExists(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Username already exists: alice", response.getBody().get("message"));
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().get("status"));
    }

    @Test
    void handleGeneralExceptionShouldReturnInternalServerErrorResponse() {
        ResponseEntity<Map<String, Object>> response = handler.handleGeneralException(new RuntimeException("boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody().get("message"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().get("status"));
        assertNotNull(response.getBody().get("timestamp"));
    }
}