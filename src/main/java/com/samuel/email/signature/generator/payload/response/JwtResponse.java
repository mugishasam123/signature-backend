package com.samuel.email.signature.generator.payload.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
  private String token;
  private Long id;
  private String email;
  private List<String> roles;
}
