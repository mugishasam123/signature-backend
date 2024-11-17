//package com.samuel.email.signature.generator.controller;
//
//import com.samuel.email.signature.generator.payload.request.LoginRequest;
//import com.samuel.email.signature.generator.payload.request.SignupRequest;
//import com.samuel.email.signature.generator.payload.request.VerifyUserDto;
//import com.samuel.email.signature.generator.payload.response.JwtResponse;
//import com.samuel.email.signature.generator.payload.response.MessageResponse;
//import com.samuel.email.signature.generator.service.AuthService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.when;
//import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@WebMvcTest(AuthController.class)
//public class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private AuthService authService;
//
//    @InjectMocks
//    private AuthController authController;
//
//    private LoginRequest loginRequest;
//    private SignupRequest signupRequest;
//    private VerifyUserDto verifyUserDto;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize test data using builders
//        loginRequest = LoginRequest.builder()
//                .email("test@example.com")
//                .password("password123")
//                .build();
//
//        signupRequest = SignupRequest.builder()
//                .email("test@example.com")
//                .password("password123")
//                .username("John")
//                .build();
//
//        verifyUserDto = VerifyUserDto.builder()
//                .email("test@example.com")
//                .verificationCode("verificationCode123")
//                .build();
//    }
//
//    @Test
//    void authenticateUser_ShouldReturnJwtResponse() throws Exception {
//        JwtResponse jwtResponse = JwtResponse.builder()
//                .token("fake-jwt-token")
//                .build();
//
//        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(jwtResponse);
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
//    }
//
//    @Test
//    void registerUser_ShouldReturnMessageResponse() throws Exception {
//        MessageResponse messageResponse = MessageResponse.builder()
//                .message("User registered successfully")
//                .build();
//
//        when(authService.registerUser(any(SignupRequest.class))).thenReturn(messageResponse);
//
//        mockMvc.perform(post("/api/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\": \"test@example.com\", \"password\": \"password123\", \"firstName\": \"John\", \"lastName\": \"Doe\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("User registered successfully"));
//    }
//
//    @Test
//    void verifyUser_ShouldReturnSuccessMessage() throws Exception {
//        MessageResponse messageResponse = MessageResponse.builder()
//                .message("Account verified successfully")
//                .build();
//
//        when(authService.verifyUser(any(VerifyUserDto.class))).thenReturn(messageResponse);
//
//        mockMvc.perform(post("/api/auth/verify")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\": \"test@example.com\", \"verificationCode\": \"verificationCode123\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Account verified successfully"));
//    }
//
//    @Test
//    void resendVerificationCode_ShouldReturnSuccessMessage() throws Exception {
//        MessageResponse messageResponse = MessageResponse.builder()
//                .message("Verification code sent")
//                .build();
//
//        when(authService.resendVerificationCode(any(String.class))).thenReturn(messageResponse);
//
//        mockMvc.perform(post("/api/auth/resend")
//                        .param("email", "test@example.com"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Verification code sent"));
//    }
//
//    @Test
//    void authenticateUser_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
//        when(authService.authenticateUser(any(LoginRequest.class))).thenThrow(new RuntimeException("Authentication error"));
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    void registerUser_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
//        when(authService.registerUser(any(SignupRequest.class))).thenThrow(new RuntimeException("Registration error"));
//
//        mockMvc.perform(post("/api/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\": \"test@example.com\", \"password\": \"password123\", \"firstName\": \"John\", \"lastName\": \"Doe\"}"))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    void verifyUser_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
//        when(authService.verifyUser(any(VerifyUserDto.class))).thenThrow(new RuntimeException("Verification error"));
//
//        mockMvc.perform(post("/api/auth/verify")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\": \"test@example.com\", \"verificationCode\": \"verificationCode123\"}"))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    void resendVerificationCode_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
//        when(authService.resendVerificationCode(any(String.class))).thenThrow(new RuntimeException("Resend error"));
//
//        mockMvc.perform(post("/api/auth/resend")
//                        .param("email", "test@example.com"))
//                .andExpect(status().isInternalServerError());
//    }
//}
