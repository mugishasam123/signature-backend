package com.samuel.email.signature.generator.controller;

import com.samuel.email.signature.generator.payload.request.LoginRequest;
import com.samuel.email.signature.generator.payload.request.SignupRequest;
import com.samuel.email.signature.generator.payload.request.VerifyUserDto;
import com.samuel.email.signature.generator.payload.response.JwtResponse;
import com.samuel.email.signature.generator.payload.response.MessageResponse;
import com.samuel.email.signature.generator.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private VerifyUserDto verifyUserDto;
    private JwtResponse jwtResponse;
    private MessageResponse messageResponse;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .email("user@example.com")
                .password("password123")
                .build();

        signupRequest = SignupRequest.builder()
                .email("user@example.com")
                .password("password123")
                .username("John")
                .build();

        verifyUserDto = VerifyUserDto.builder()
                .email("user@example.com")
                .verificationCode("verificationCode")
                .build();

        jwtResponse = JwtResponse.builder()
                .token("token123")
                .build();

        messageResponse = MessageResponse.builder()
                .message("Success")
                .build();
    }

    @Test
    void authenticateUser_ShouldReturnJwtResponse() {
        when(authService.authenticateUser(loginRequest)).thenReturn(jwtResponse);
        ResponseEntity<JwtResponse> response = authController.authenticateUser(loginRequest);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(jwtResponse, response.getBody());
        verify(authService, times(1)).authenticateUser(loginRequest);
    }

    @Test
    void authenticateUser_ShouldReturnInternalServerErrorOnException() {
        when(authService.authenticateUser(loginRequest)).thenThrow(new RuntimeException("Error"));
        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            authController.authenticateUser(loginRequest);
        });
        assertEquals("java.lang.RuntimeException: Error", thrownException.getMessage());
    }



    @Test
    void registerUser_ShouldReturnMessageResponse() {
        when(authService.registerUser(signupRequest)).thenReturn(messageResponse);
        ResponseEntity<MessageResponse> response = authController.registerUser(signupRequest);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(messageResponse, response.getBody());
        verify(authService, times(1)).registerUser(signupRequest);
    }

    @Test
    void registerUser_ShouldReturnInternalServerErrorOnException() {
        when(authService.registerUser(signupRequest)).thenThrow(new RuntimeException("Error"));
        try {
            authController.registerUser(signupRequest);
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            assertEquals("java.lang.RuntimeException: Error", e.getMessage());
        }
    }

}
