package com.samuel.email.signature.generator.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUserBuilderAndGetterSetter() {
        Role role = Role.builder()
                .id(1)
                .name(ERole.ROLE_USER)
                .build();
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = User.builder()
                .id(1L)
                .username("samuel")
                .phoneNumber("123456789")
                .userTitle("Developer")
                .email("samuel@example.com")
                .password("password123")
                .verificationCode("code123")
                .verificationCodeExpiresAt(LocalDateTime.now().plusHours(1))
                .enabled(true)
                .roles(roles)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("samuel", user.getUsername());
        assertEquals("123456789", user.getPhoneNumber());
        assertEquals("Developer", user.getUserTitle());
        assertEquals("samuel@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("code123", user.getVerificationCode());
        assertNotNull(user.getVerificationCodeExpiresAt());
        assertTrue(user.isEnabled());
        assertEquals(roles, user.getRoles());
    }

    @Test
    void testValidationConstraints() {
        User user = User.builder()
                .username("")
                .email("invalidEmail")
                .password("")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testAuthorities() {
        User user = new User();
        assertNotNull(user.getAuthorities());
        assertTrue(user.getAuthorities().isEmpty());
    }

    @Test
    void testAccountStatusFlags() {
        User user = new User();
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertFalse(user.isEnabled());

        user.setEnabled(true);
        assertTrue(user.isEnabled());
    }

    @Test
    void testRolesAssociation() {
        Role role1 = Role.builder().id(1).name(ERole.ROLE_USER).build();
        Role role2 = Role.builder().id(2).name(ERole.ROLE_ADMIN).build();

        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);

        User user = User.builder()
                .roles(roles)
                .build();

        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains(role1));
        assertTrue(user.getRoles().contains(role2));
    }

}
