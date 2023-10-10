package com.axonactive.agileskills.user.service.model;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.user.entity.RoleEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.axonactive.agileskills.base.exception.ErrorMessage.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private Long id;

    @Size(max = MAX_SIZE_NAME, message = USER_NAME_LENGTH_CONSTRAINT)
    private String name;

    @NotBlank(message = EMAIL_BLANK_OR_NULL)
    @Email(message = EMAIL_WRONG_FORMAT)
    private String email;

    @NotBlank(message = PASSWORD_BLANK_OR_NULL)
    private String password;

    private StatusEnum status;

    private RoleEnum role;
}
