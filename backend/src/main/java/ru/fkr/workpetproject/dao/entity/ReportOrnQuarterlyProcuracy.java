package ru.fkr.workpetproject.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "report_orn_quarterly_procuracy")
public class ReportOrnQuarterlyProcuracy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_to")
    private LocalDate dateTo;

    @Column(name = "house_id_reg")
    private Long houseIdReg;

    @Column(name = "fees_period")
    private BigDecimal feesPeriod;

    @Column(name = "penalties_period")
    private BigDecimal penaltiesPeriod;

    @Column(name = "payments_period")
    private BigDecimal paymentsPeriod;

    @Column(name = "penalties_payments_period")
    private BigDecimal penaltiesPaymentsPeriod;

    @Column(name = "spent_money")
    private BigDecimal spentMoney;
}