package com.axonactive.agileskills.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedDataResponse {
    private List<?> listData;
    private PaginationData paginationData;
}
