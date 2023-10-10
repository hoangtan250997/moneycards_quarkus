package com.axonactive.agileskills.base.security.rest.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static com.axonactive.agileskills.base.exception.ErrorMessage.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtRequest {

    @NotBlank(message = EMAIL_BLANK_OR_NULL)
    @Email(message = EMAIL_WRONG_FORMAT)
    private String email;

    @NotBlank(message = PASSWORD_BLANK_OR_NULL)
    private String password;
}
