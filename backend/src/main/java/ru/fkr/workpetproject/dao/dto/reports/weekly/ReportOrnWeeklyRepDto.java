package ru.fkr.workpetproject.dao.dto.reports.weekly;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ReportOrnWeeklyRepDto {
    private Long id;
    private LocalDate reportDate;
    private String reportType;
    private Integer decisionId;
    private String division;
    private Integer lsCount;
    private BigDecimal totalAmount;
    private Integer innCount;
}
