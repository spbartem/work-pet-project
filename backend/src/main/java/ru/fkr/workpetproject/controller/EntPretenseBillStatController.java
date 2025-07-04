package ru.fkr.workpetproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.fkr.workpetproject.dao.entity.EntPretenseBillStat;
import ru.fkr.workpetproject.service.EntPretenseBillStatService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/ent-pretense-bill-stat")
public class EntPretenseBillStatController {

    @Autowired
    private EntPretenseBillStatService entPretenseBillStatService;

    @GetMapping
    public Page<EntPretenseBillStat> getEntPretenseBillStat(
            @RequestParam(required = false) String billDate,
            Pageable pageable) {
        LocalDate date = billDate != null ? LocalDate.parse(billDate) : null;
        return entPretenseBillStatService.getEntPretenseBillStat(date, pageable);
    }
}
