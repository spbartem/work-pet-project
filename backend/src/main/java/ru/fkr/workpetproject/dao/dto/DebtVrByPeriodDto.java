package ru.fkr.workpetproject.dao.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DebtVrByPeriodDto {
    private Integer res;
    private String errMess;
    private Long id;
    private String type;
    private String division;
    private Integer accountCount;
    private BigDecimal totalAmount;
}
