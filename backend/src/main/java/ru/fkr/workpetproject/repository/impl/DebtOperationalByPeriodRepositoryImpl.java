package ru.fkr.workpetproject.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.fkr.workpetproject.dao.dto.DebtOperationalByPeriodDto;
import ru.fkr.workpetproject.repository.custom.DebtOperationalByPeriodRepositoryCustom;

import java.math.BigDecimal;


@Repository
public class DebtOperationalByPeriodRepositoryImpl implements DebtOperationalByPeriodRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DebtOperationalByPeriodDto getDebtOperationalByPeriod() {
        String sql = """
             SELECT
                calculate_date AS reportDate, 
                debtsum AS debtOperationalTotal,
                SUM(less_3m + less_3mul) AS debtOperationalLess3m,
                SUM(int_3m_7m + int_3m_7mul) AS debtOperationalBetween3mAnd7m,
                SUM(int_7m_2y + int_7m_2yul) AS debtOperationalBetween7mAnd2y,
                SUM(debtsum - (less_3m + less_3mul + int_3m_7m + int_3m_7mul + int_7m_2y
                + int_7m_2yul + more_3y + more_3yul)) AS debtOperationalBetween2yAnd3y,
                SUM(more_3y + more_3yul) AS debtOperationalMore3y
                FROM v_f_dolgreport_by_feelink_byperiod
                GROUP BY calculate_date, debtsum
            ORDER BY calculate_date
            DESC LIMIT 1
            """;

        Object[] result = (Object[]) entityManager.createNativeQuery(sql).getSingleResult();

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
}
