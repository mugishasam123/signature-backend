package com.samuel.email.signature.generator.service;

import com.samuel.email.signature.generator.models.ERole;
import com.samuel.email.signature.generator.models.Role;
import com.samuel.email.signature.generator.models.User;
import com.samuel.email.signature.generator.payload.request.LoginRequest;
import com.samuel.email.signature.generator.payload.request.SignupRequest;
import com.samuel.email.signature.generator.payload.request.VerifyUserDto;
import com.samuel.email.signature.generator.payload.response.JwtResponse;
import com.samuel.email.signature.generator.payload.response.MessageResponse;
import com.samuel.email.signature.generator.repository.RoleRepository;
import com.samuel.email.signature.generator.repository.UserRepository;
import com.samuel.email.signature.generator.security.jwt.JwtUtils;
import com.samuel.email.signature.generator.security.services.UserDetailsImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private EmailService emailService;

    AuthServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser() {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "password");

        User user = new User();
        user.setEmail(loginRequest.getEmail());
        user.setEnabled(true);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(
                new UserDetailsImpl(
                        1L,
                        "user",
                        "user@example.com",
                        "password",
                        Set.of()
                )
        );

        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");

        JwtResponse response = authService.authenticateUser(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("user@example.com", response.getEmail());
    }

    @Test
    void testRegisterUser() throws MessagingException {
        SignupRequest signUpRequest = SignupRequest.builder()
                .username("user")
                .email("user@example.com")
                .password("password")
                .build();

        Role role = Role.builder()
                .id(1)
                .name(ERole.ROLE_USER)
                .build();

        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(encoder.encode(signUpRequest.getPassword())).thenReturn("encoded-password");
        MessageResponse response = authService.registerUser(signUpRequest);
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendVerificationEmail(eq("user@example.com"), anyString(), anyString());
        assertNotNull(response);
        assertEquals("User registered successfully! Please check your email to verify your account and activate it.", response.getMessage());
    }

    @Test
    void testVerifyUser() {
        VerifyUserDto verifyUserDto = new VerifyUserDto("user@example.com", "123456");
        User user = new User();
        user.setEmail(verifyUserDto.getEmail());
        user.setVerificationCode("123456");
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        when(userRepository.findByEmail(verifyUserDto.getEmail())).thenReturn(Optional.of(user));
        authService.verifyUser(verifyUserDto);
        verify(userRepository, times(1)).save(user);

        assertTrue(user.isEnabled());
        assertNull(user.getVerificationCode());
        assertNull(user.getVerificationCodeExpiresAt());
    }

    @Test
    void testResendVerificationCode() throws MessagingException {
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        user.setEnabled(false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        authService.resendVerificationCode(email);

        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1)).sendVerificationEmail(eq(email), anyString(), anyString());
        assertNotNull(user.getVerificationCode());
        assertNotNull(user.getVerificationCodeExpiresAt());
        assertTrue(user.getVerificationCodeExpiresAt().isAfter(LocalDateTime.now()));
    }
}
