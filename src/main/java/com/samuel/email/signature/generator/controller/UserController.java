package com.samuel.email.signature.generator.controller;

import com.samuel.email.signature.generator.payload.request.UpdateUserRequest;
import com.samuel.email.signature.generator.payload.response.MessageResponse;
import com.samuel.email.signature.generator.security.services.UserDetailsImpl;
import com.samuel.email.signature.generator.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.samuel.email.signature.generator.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UsersService usersService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<User>> getCompany() {
    try {
      List<User> company = usersService.findAll();
      return ResponseEntity.ok(company);
    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }

  }

  @GetMapping("/me")
  public ResponseEntity<User> whoami() {

    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      User currentUser = usersService.findByEmail(userDetails.getEmail());
      User user = User.builder()
          .id(currentUser.getId())
          .username(currentUser.getUsername())
          .email(currentUser.getEmail())
          .roles(currentUser.getRoles())
          .phoneNumber(currentUser.getPhoneNumber())
          .userTitle(currentUser.getUserTitle())
          .password("*********")
          .build();
      return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }

  }

  @PutMapping("/phone")
  public ResponseEntity<MessageResponse> updatePhoneNumber(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
    try {
      MessageResponse messageResponse = usersService.updatePhoneNumber(updateUserRequest);
      return ResponseEntity.ok(messageResponse);
    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }

  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/userTitle")
  public ResponseEntity<MessageResponse> updateUserTitle(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
    try {
      MessageResponse messageResponse = usersService.updateUserTitle(updateUserRequest);
      return ResponseEntity.ok(messageResponse);
    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }

  }
}
