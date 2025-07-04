package ru.fkr.workpetproject.repository.reports;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.fkr.workpetproject.dao.dto.reports.weekly.ReportOrnWeeklyRepDto;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ReportOrnWeeklyRepRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReportOrnWeeklyRepRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReportOrnWeeklyRepDto> getWeeklyRep(LocalDate reportDate) {
        return jdbcTemplate.query(
                "SELECT * FROM f_art_report_orn_weekly_rep(?)",
                ps -> {
                    if (reportDate != null) {
                        ps.setDate(1, java.sql.Date.valueOf(reportDate));
                    } else {
                        ps.setNull(1, java.sql.Types.DATE);
                    }
                },
                (rs, rowNum) -> {
                    ReportOrnWeeklyRepDto entry = new ReportOrnWeeklyRepDto();
                    entry.setId(rs.getLong("id"));
                    entry.setReportDate(rs.getDate("report_date").toLocalDate());
                    entry.setReportType(rs.getString("report_type"));
                    entry.setDecisionId(rs.getInt("decision_id"));
                    entry.setDivision(rs.getString("division"));
                    entry.setLsCount(rs.getInt("ls_count"));
                    entry.setTotalAmount(rs.getBigDecimal("total_amount"));
                    entry.setInnCount(rs.getInt("inn_count"));
                    return entry;
                }
        );
    }
}
