package ru.fkr.workpetproject.service.debt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fkr.workpetproject.dao.dto.DebtOperationalByPeriodDto;
import ru.fkr.workpetproject.repository.impl.DebtOperationalByPeriodRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

@Service
public class DebtOperationalByPeriodService {
    @Autowired
    private DebtOperationalByPeriodRepositoryImpl debtOperationalByPeriodRepositoryImpl;

    public DebtOperationalByPeriodDto getDebtOperationalByPeriod(LocalDate date) {
        return debtOperationalByPeriodRepositoryImpl.getDebtOperationalByPeriod(date);
    }

    public List<LocalDate> getAvailableReportDates() {
        return debtOperationalByPeriodRepositoryImpl.getAvailableReportDates();
    }
}
