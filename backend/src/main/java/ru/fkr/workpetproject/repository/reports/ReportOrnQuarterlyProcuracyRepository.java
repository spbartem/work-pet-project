package ru.fkr.workpetproject.repository.reports;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fkr.workpetproject.dao.entity.ReportOrnQuarterlyProcuracy;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportOrnQuarterlyProcuracyRepository extends JpaRepository<ReportOrnQuarterlyProcuracy, Long> {

    @Query(value = "SELECT d.name AS districtName, h.fullAddress AS house_full_address, " +
            "SUM(COALESCE(r.feesPeriod, 0)) + SUM(COALESCE(r.penaltiesPeriod, 0)) AS fees_period, " +
            "SUM(COALESCE(r.paymentsPeriod, 0)) + SUM(COALESCE(r.penaltiesPaymentsPeriod, 0)) AS payments_period, " +
            "SUM(COALESCE(r.spentMoney, 0)) AS spent_money " +
            "FROM ReportOrnQuarterlyProcuracy r " +
            "JOIN ExtHouseReg h ON r.houseIdReg = h.houseId " +
            "JOIN ExtDistrictReg d ON h.districtId.id = d.id " +
            "WHERE r.dateTo = :date " +
            "GROUP BY d.name, h.fullAddress " +
            "ORDER BY d.name, h.fullAddress")
    List<Object[]> getQuarterlyProcuracyCollectability(@Param("date") LocalDate date);

    @Query(value = "SELECT calcDate AS calcDate, rototaldebt AS roTotalDebt, " +
            "debtless3y as roDebtLess3y " +
            "FROM f_art_report_orn_quarterly_procuracy_debt(:date) v ",
            nativeQuery = true)
    List<Object[]> getQuarterlyProcuracyDebt(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT r.dateTo FROM ReportOrnQuarterlyProcuracy r ORDER BY r.dateTo DESC")
    List<LocalDate> getAvailableReportDates();

    @Query(value = "SELECT * FROM f_art_report_orn_quarterly_procuracy_check()", nativeQuery = true)
    Map<String, Object> checkFillAvailability();

    @Query(value = "SELECT age(clock_timestamp(), query_start) AS duration, true AS processed " +
            "FROM pg_stat_activity " +
            "WHERE state = 'active' AND lower(query) LIKE 'select * from f_art_report_orn_quarterly_procuracy_fill()%'",
            nativeQuery = true)
    Map<String, Object> checkProgressStatusReportOrnQuarterlyProcuracy();

    @Transactional
    @Query(value = "SELECT * FROM f_art_report_orn_quarterly_procuracy_fill();", nativeQuery = true)
    Map<String, Object> fillReport();
}
