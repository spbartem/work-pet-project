package ru.fkr.workpetproject.controller.debt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fkr.workpetproject.dao.dto.DebtOperationalByPeriodDto;
import ru.fkr.workpetproject.service.debt.DebtOperationalByPeriodService;

@RestController
@RequestMapping("/api/debt")
public class DebtController {
    @Autowired
    private DebtOperationalByPeriodService debtOperationalByPeriodService;

    @GetMapping("/operational-by-period")
    public DebtOperationalByPeriodDto getDebtOperationalByPeriod() {
        return debtOperationalByPeriodService.getDebtOperationalByPeriod();
    }
}
