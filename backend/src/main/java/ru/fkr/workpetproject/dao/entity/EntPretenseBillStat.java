package ru.fkr.workpetproject.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "ent_pretense_bill_stat")
public class EntPretenseBillStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_date")
    private LocalDate billDate;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @Column(name = "debt_ls_count")
    private Integer debtLsCount;

    @Column(name = "due_amount")
    private BigDecimal dueAmount;

    @Column(name = "debt_amount")
    private BigDecimal debtAmount;

    @Column(name = "debt_partially_pay_count")
    private Integer debtPartiallyPayCount;

    @Column(name = "debt_partially_pay_amount")
    private BigDecimal debtPartiallyPayAmount;

    @Column(name = "debt_full_pay_count")
    private Integer debtFullPayCount;

    @Column(name = "debt_full_pay_amount")
    private BigDecimal debtFullPayAmount;

    @Column(name = "debt_total_count")
    private Integer debtTotalCount;

    @Column(name = "debt_total_amount")
    private BigDecimal debtTotalAmount;
}
