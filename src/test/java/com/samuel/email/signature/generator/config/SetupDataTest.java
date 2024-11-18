package com.samuel.email.signature.generator.config;

import com.samuel.email.signature.generator.models.ERole;
import com.samuel.email.signature.generator.models.Role;
import com.samuel.email.signature.generator.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SetupDataTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private SetupData setupData;

    private Role roleUser;
    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleUser = Role.builder().name(ERole.ROLE_USER).build();
        roleAdmin = Role.builder().name(ERole.ROLE_ADMIN).build();
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(java.util.Optional.ofNullable(roleUser));
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(java.util.Optional.ofNullable(roleAdmin));
    }


    @Test
    void testCreateRoleIfNotExists_ShouldNotCreateRoleIfAlreadyExists() {
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(java.util.Optional.of(roleUser));
        setupData.createRoleIfNotExists(ERole.ROLE_USER);
        verify(roleRepository, times(0)).save(any(Role.class));
    }

    @Test
    void testCreateAdminUser_ShouldThrowExceptionIfRolesNotFound() {
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(java.util.Optional.empty());
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(java.util.Optional.empty());
        assertThrows(RuntimeException.class, () -> setupData.createAdminUser());
    }
}
