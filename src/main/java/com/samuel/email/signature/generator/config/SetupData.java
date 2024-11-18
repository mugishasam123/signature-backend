package com.samuel.email.signature.generator.config;

import com.samuel.email.signature.generator.models.Role;
import com.samuel.email.signature.generator.models.ERole;
import com.samuel.email.signature.generator.models.User;
import com.samuel.email.signature.generator.repository.RoleRepository;
import com.samuel.email.signature.generator.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SetupData {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @PostConstruct
    public void init() {
        createRoleIfNotExists(ERole.ROLE_USER);
        createRoleIfNotExists(ERole.ROLE_ADMIN);

        if (!userRepository.existsByUsername("admin")) {
            createAdminUser();
        }
    }

    public void createRoleIfNotExists(ERole roleName) {
        roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(Role.builder().name(roleName).build()));
    }

    public void createAdminUser() {
        Role roleAdmin = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Admin role not found."));
        Role roleUser = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: User role not found."));

        User adminUser = User.builder()
                .username("admin")
                .email("admin@ist.com")
                .enabled(true)
                .password(encoder.encode("admin123"))
                .roles(new HashSet<>(Set.of(roleAdmin, roleUser)))
                .build();

        userRepository.save(adminUser);
    }
}
