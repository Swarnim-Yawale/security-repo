package com.example.demo.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void defaultConstructor_shouldCreateEmptyUser() {
        User user = new User();

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
    }

    @Test
    void parameterizedConstructor_shouldSetUsernameAndPassword() {
        User user = new User("alice", "secret");

        assertEquals("alice", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertNull(user.getId());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        User user = new User();

        user.setId(10L);
        user.setUsername("bob");
        user.setPassword("hashed-password");

        assertEquals(10L, user.getId());
        assertEquals("bob", user.getUsername());
        assertEquals("hashed-password", user.getPassword());
    }
}