package ru.fkr.workpetproject.service.debt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fkr.workpetproject.dao.dto.DebtVrByPeriodDto;
import ru.fkr.workpetproject.repository.debt.DebtVrByPeriodRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class DebtVrByPeriodService {

    @Autowired
    private DebtVrByPeriodRepository debtVrByPeriodRepository;

    public List<DebtVrByPeriodDto> getDebtVrByPeriod(LocalDate localDate, Integer decisionType) {
        return debtVrByPeriodRepository.getDebtVrByPeriod(localDate, decisionType);
    }

    public List<LocalDate> getAvailableReportDate() {
        return debtVrByPeriodRepository.getAvailableReportDates();
    }
}
