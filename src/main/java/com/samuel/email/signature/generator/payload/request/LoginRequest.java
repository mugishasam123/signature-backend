package com.samuel.email.signature.generator.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
  @NotBlank
  private String email;

  @NotBlank
  private String password;
}
