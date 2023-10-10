package com.axonactive.agileskills.position.service.model;

import com.axonactive.agileskills.position.entity.PositionStatusEnum;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredSkill;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

import static com.axonactive.agileskills.base.exception.ErrorMessage.*;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Position {

    private static final String OFFSET_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private Long id;

    @NotBlank(message = KEY_POSITION_NAME_NULL_OR_BLANK)
    @Size(min = MIN_SIZE_NAME, max = MAX_SIZE_NAME, message = KEY_POSITION_NAME_LENGTH_CONSTRAINT)
    private String name;

    @Size(max = MAX_SIZE_NOTE, message = KEY_POSITION_NOTE_LENGTH_CONSTRAINT)
    private String note;

    @Enumerated(EnumType.STRING)
    private PositionStatusEnum status;

    @NotNull(message = KEY_POSITION_QUANTITY_NULL_OR_BLANK)
    @Min(value = MIN_QUANTITY, message = KEY_POSITION_QUANTITY_GREATER_THAN_0)
    @Max(value = MAX_QUANTITY, message = KEY_POSITION_QUANTITY_LESS_THAN_100)
    private Integer quantity;

    private Long teamId;
    private String teamName;
    private String departmentName;
    private List<RequiredSkill> requiredSkillList;
    private OffsetDateTime openedDate;
    private OffsetDateTime createdDate;
    private OffsetDateTime closedDate;
}
