package ru.fkr.workpetproject.dao.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DebtOperationalByPeriodDto {
    private LocalDate reportDate;
    private BigDecimal debtOperationalTotal;
    private BigDecimal debtOperationalLess3m;
    private BigDecimal debtOperationalBetween3mAnd7m;
    private BigDecimal debtOperationalBetween7mAnd2y;
    private BigDecimal debtOperationalBetween2yAnd3y;
    private BigDecimal debtOperationalMore3y;

    public DebtOperationalByPeriodDto(LocalDate reportDate, BigDecimal debtOperationalTotal,
                                      BigDecimal debtOperationalLess3m,
                                      BigDecimal debtOperationalBetween3mAnd7m,
                                      BigDecimal debtOperationalBetween7mAnd2y,
                                      BigDecimal debtOperationalBetween2yAnd3y, BigDecimal debtOperationalMore3y) {
        this.reportDate = reportDate;
        this.debtOperationalTotal = debtOperationalTotal;
        this.debtOperationalLess3m = debtOperationalLess3m;
        this.debtOperationalBetween3mAnd7m = debtOperationalBetween3mAnd7m;
        this.debtOperationalBetween7mAnd2y = debtOperationalBetween7mAnd2y;
        this.debtOperationalBetween2yAnd3y = debtOperationalBetween2yAnd3y;
        this.debtOperationalMore3y = debtOperationalMore3y;
    }
}
