package com.axonactive.agileskills.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationData {
    private Integer totalRecords;
    private Integer pageSize;
    private Integer totalPages;
    private Integer currentPage;
}
