package com.samuel.email.signature.generator.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {
    @Test
    void testRoleBuilderAndGetterSetter() {
        Role role = Role.builder()
                .id(1)
                .name(ERole.ROLE_USER)
                .build();

        assertEquals(1, role.getId());
        assertEquals(ERole.ROLE_USER, role.getName());

        role.setId(2);
        role.setName(ERole.ROLE_ADMIN);
        assertEquals(2, role.getId());
        assertEquals(ERole.ROLE_ADMIN, role.getName());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_USER);

        assertEquals(1, role.getId());
        assertEquals(ERole.ROLE_USER, role.getName());
    }

    @Test
    void testAllArgsConstructor() {
        Role role = new Role(1, ERole.ROLE_USER);
        assertEquals(1, role.getId());
        assertEquals(ERole.ROLE_USER, role.getName());
    }

}
