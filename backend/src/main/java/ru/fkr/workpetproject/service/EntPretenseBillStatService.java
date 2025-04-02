package ru.fkr.workpetproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.fkr.workpetproject.dao.entity.EntPretenseBillStat;
import ru.fkr.workpetproject.repository.EntPretenseBillStatRepository;

import java.time.LocalDate;

@Service
public class EntPretenseBillStatService {

    @Autowired
    private EntPretenseBillStatRepository entPretenseBillStatRepository;

    public Page<EntPretenseBillStat> getEntPretenseBillStat(LocalDate billDate, Pageable pageable) {
        if (billDate != null) {
            return entPretenseBillStatRepository.findByBillDate(billDate, pageable);
        }
        return entPretenseBillStatRepository.findAll(pageable);
    }
}
