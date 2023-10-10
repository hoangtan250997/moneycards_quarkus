package com.axonactive.agileskills.department.service.model;

import com.axonactive.agileskills.base.entity.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.axonactive.agileskills.base.exception.ErrorMessage.*;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {


    private Long id;

    @NotBlank(message = DEPARTMENT_NAME_NULL_OR_BLANK)
    @Size(max = MAX_SIZE_NAME, message = DEPARTMENT_NAME_LENGTH_CONSTRAINT)
    private String name;

    private StatusEnum status;
}
