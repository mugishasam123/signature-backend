package com.samuel.email.signature.generator.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyUserDto {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String verificationCode;
}