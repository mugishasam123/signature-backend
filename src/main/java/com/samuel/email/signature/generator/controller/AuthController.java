package com.samuel.email.signature.generator.controller;

import com.samuel.email.signature.generator.payload.request.VerifyUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samuel.email.signature.generator.payload.request.LoginRequest;
import com.samuel.email.signature.generator.payload.request.SignupRequest;
import com.samuel.email.signature.generator.payload.response.JwtResponse;
import com.samuel.email.signature.generator.payload.response.MessageResponse;
import com.samuel.email.signature.generator.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        try {
            MessageResponse messageResponse = authService.registerUser(signUpRequest);
            return ResponseEntity.ok(messageResponse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<MessageResponse> verifyUser(@Valid @RequestBody VerifyUserDto verifyUserDto) {
        try {
            authService.verifyUser(verifyUserDto);
            return ResponseEntity.ok(MessageResponse.builder().message("Account verified successfully").build());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<MessageResponse> resendVerificationCode(@RequestParam String email) {
        try {
            authService.resendVerificationCode(email);
            return ResponseEntity.ok(MessageResponse.builder().message("Verification code sent").build());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
