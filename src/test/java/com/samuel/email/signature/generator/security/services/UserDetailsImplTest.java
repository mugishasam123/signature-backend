package com.samuel.email.signature.generator.security.services;

import com.samuel.email.signature.generator.models.ERole;
import com.samuel.email.signature.generator.models.Role;
import com.samuel.email.signature.generator.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        User mockUser =  User.builder()
                .id(1L)
                .username("testuser")
                .email("testuser@example.com")
                .password("password123")
                .roles(Set.of(
                        Role.builder().name(ERole.ROLE_USER).build(),
                        Role.builder().name(ERole.ROLE_ADMIN).build()
                ))
                .build();

        userDetails = UserDetailsImpl.build(mockUser);
    }

    @Test
    void testGetUsername() {
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    void testGetPassword() {
        assertEquals("password123", userDetails.getPassword());
    }

    @Test
    void testGetEmail() {
        assertEquals("testuser@example.com", userDetails.getEmail());
    }

    @Test
    void testGetAuthorities() {
        List<GrantedAuthority> expectedAuthorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        assertTrue(userDetails.getAuthorities().containsAll(expectedAuthorities) &&
                expectedAuthorities.containsAll(userDetails.getAuthorities()));
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testEquals_SameObject() {
        assertTrue(userDetails.equals(userDetails));
    }

    @Test
    void testEquals_DifferentObject() {
        UserDetailsImpl otherUserDetails = new UserDetailsImpl(1L, "testuser", "testuser@example.com", "password123", userDetails.getAuthorities());
        assertTrue(userDetails.equals(otherUserDetails));
    }

    @Test
    void testEquals_DifferentId() {
        UserDetailsImpl otherUserDetails = new UserDetailsImpl(2L, "testuser", "testuser@example.com", "password123", userDetails.getAuthorities());
        assertFalse(userDetails.equals(otherUserDetails));
    }

    @Test
    void testEquals_NullObject() {
        assertFalse(userDetails.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        assertFalse(userDetails.equals(new Object()));
    }
}
