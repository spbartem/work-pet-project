package ru.fkr.workpetproject.controller.debt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fkr.workpetproject.dao.dto.DebtOperationalByPeriodDto;
import ru.fkr.workpetproject.dao.dto.DebtVrByPeriodDto;
import ru.fkr.workpetproject.service.debt.DebtOperationalByPeriodService;
import ru.fkr.workpetproject.service.debt.DebtVrByPeriodService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/debt")
public class DebtController {
    @Autowired
    private DebtOperationalByPeriodService debtOperationalByPeriodService;

    @Autowired
    private DebtVrByPeriodService debtVrByPeriodService;

    @GetMapping("/operational-by-period")
    public DebtOperationalByPeriodDto getDebtOperationalByPeriod(
            @RequestParam LocalDate localDate
    ) {
        return debtOperationalByPeriodService.getDebtOperationalByPeriod(localDate);
    }

    @GetMapping("/vr-by-period")
    public List<DebtVrByPeriodDto> getDebtVrByPeriod(
            @RequestParam LocalDate localDate,
            @RequestParam Integer decisionType
    ) {
        return debtVrByPeriodService.getDebtVrByPeriod(localDate, decisionType);
    }

    @GetMapping("/availableDatesVr")
    public List<LocalDate> getAvailableReportDatesVr() {
        return debtVrByPeriodService.getAvailableReportDate();
    }

    @GetMapping("/availableDatesOperational")
    public List<LocalDate> getAvailableReportDatesOperational() { return debtOperationalByPeriodService.getAvailableReportDates(); }
}
