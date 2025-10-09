package ru.fkr.workpetproject.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.fkr.workpetproject.dao.dto.DebtOperationalByPeriodDto;
import ru.fkr.workpetproject.repository.custom.DebtOperationalByPeriodRepositoryCustom;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class DebtOperationalByPeriodRepositoryImpl implements DebtOperationalByPeriodRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DebtOperationalByPeriodDto getDebtOperationalByPeriod(LocalDate localDate) {
        String sql = """
             SELECT
                calculate_date AS reportDate, 
                SUM(less_3m + less_3mul + int_3m_7m + int_3m_7mul + int_7m_3y + int_7m_3yul + more_3y + more_3yul) AS debtOperationalTotal,
                SUM(less_3m + less_3mul) AS debtOperationalLess3m,
                SUM(int_3m_7m + int_3m_7mul) AS debtOperationalBetween3mAnd7m,
                SUM(int_7m_2y + int_7m_2yul) AS debtOperationalBetween7mAnd2y,
                SUM(int_7m_3y + int_7m_3yul - int_7m_2y - int_7m_2yul) AS debtOperationalBetween2yAnd3y,
                SUM(more_3y + more_3yul) AS debtOperationalMore3y
            FROM f_dolgreport_by_feelink_period(1)
            WHERE calculate_date = ?
            GROUP BY calculate_date
            """;

        Object[] result = (Object[]) entityManager
                .createNativeQuery(sql)
                .setParameter(1, localDate) // <- Установка параметра по индексу (позиционный параметр)
                .getSingleResult();

        // Маппинг результата в DTO
        return new DebtOperationalByPeriodDto(
                ((java.sql.Date) result[0]).toLocalDate(),
                new BigDecimal(result[1].toString()), // всего
                new BigDecimal(result[2].toString()), // до_3_месяцев
                new BigDecimal(result[3].toString()), // от_3_месяцев_до_7_месяцев
                new BigDecimal(result[4].toString()), // от_7_месяцев_до_2_лет
                new BigDecimal(result[5].toString()), // от_2_лет_до_3_лет
                new BigDecimal(result[6].toString())  // более_3_лет
        );
    }

    public List<LocalDate> getAvailableReportDates() {
        List<?> resultList = entityManager.createNativeQuery(
                "SELECT calculate_date FROM v_dolgReport_by_vr2981_byFeeLink").getResultList();

        return resultList.stream()
                .map(obj -> ((java.sql.Date) obj).toLocalDate())
                .collect(Collectors.toList());
    }
}
