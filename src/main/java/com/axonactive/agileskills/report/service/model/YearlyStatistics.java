package com.axonactive.agileskills.report.service.model;

import lombok.*;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class YearlyStatistics {
    private String skillName;
    private Double appearancePercentage;
    private Double mustHavePercentage;
    private Double niceToHavePercentage;

}
