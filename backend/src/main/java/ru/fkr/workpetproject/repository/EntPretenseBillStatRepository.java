package ru.fkr.workpetproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fkr.workpetproject.dao.entity.EntPretenseBillStat;


import java.time.LocalDate;

@Repository
public interface EntPretenseBillStatRepository extends JpaRepository<EntPretenseBillStat, Long> {
    Page<EntPretenseBillStat> findByBillDate(LocalDate billDate, Pageable pageable);
}
