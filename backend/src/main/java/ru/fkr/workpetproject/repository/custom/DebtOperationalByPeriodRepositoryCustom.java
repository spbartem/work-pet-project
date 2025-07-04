package ru.fkr.workpetproject.repository.custom;

import ru.fkr.workpetproject.dao.dto.DebtOperationalByPeriodDto;

import java.time.LocalDate;

public interface DebtOperationalByPeriodRepositoryCustom {
    DebtOperationalByPeriodDto getDebtOperationalByPeriod(LocalDate date);
}
