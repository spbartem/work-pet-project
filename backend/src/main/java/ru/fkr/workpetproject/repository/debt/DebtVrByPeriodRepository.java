package ru.fkr.workpetproject.repository.debt;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.fkr.workpetproject.dao.dto.DebtVrByPeriodDto;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DebtVrByPeriodRepository {

    private final JdbcTemplate jdbcTemplate;

    public DebtVrByPeriodRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DebtVrByPeriodDto> getDebtVrByPeriod(LocalDate localDate, Integer decisionType) {
        return jdbcTemplate.query(
                "SELECT * FROM f_art_report_orn_v_debt_by_vr(?, ?)",
                ps -> {
                    ps.setObject(1, localDate);
                    ps.setObject(2, decisionType);
                    },
                (rs, rowNum) -> {
                    DebtVrByPeriodDto entry = new DebtVrByPeriodDto();
                    entry.setRes(rs.getInt("res"));
                    entry.setErrMess(rs.getString("err_mess"));
                    entry.setId(rs.getLong("id"));
                    entry.setType(rs.getString("type"));
                    entry.setDivision(rs.getString("division"));
                    entry.setAccountCount(rs.getInt("accounts_count"));
                    entry.setTotalAmount(rs.getBigDecimal("total_amount"));
                    return entry;
                }
        );
    }

    public List<LocalDate> getAvailableReportDates() {
        return jdbcTemplate.query(
                "SELECT calc_date FROM v_vr_2981_grouped_by_decision",
                (rs, rowNum) -> rs.getDate("calc_date").toLocalDate()
        );
    }
}
