package com.samuel.email.signature.generator.payload.request;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    private String userTitle;

    private String phoneNumber;

    @Email
    private String email;
}
