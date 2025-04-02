package ru.fkr.workpetproject.service.debt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fkr.workpetproject.dao.dto.DebtOperationalByPeriodDto;
import ru.fkr.workpetproject.repository.impl.DebtOperationalByPeriodRepositoryImpl;

@Service
public class DebtOperationalByPeriodService {
    @Autowired
    private DebtOperationalByPeriodRepositoryImpl debtOperationalByPeriodRepositoryImpl;

    public DebtOperationalByPeriodDto getDebtOperationalByPeriod() {
        return debtOperationalByPeriodRepositoryImpl.getDebtOperationalByPeriod();
    }
}
