package ru.fkr.workpetproject.repository.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ReportOrnQuarterlyProcuracyRepositoryImpl implements ReportOrnQuarterlyProcuracyRepositoryCustom {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Object lastDuration = null;

    @Override
    public Map<String, Object> checkProgressStatusReportOrnQuarterlyProcuracy() {
        String sql = """
            SELECT age(clock_timestamp(), query_start) AS duration
            FROM pg_stat_activity
            WHERE state = 'active'
              AND lower(query) LIKE 'select * from f_art_report_orn_quarterly_procuracy_fill()%'
            LIMIT 1
        """;

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        Map<String, Object> response = new HashMap<>();

        if (!result.isEmpty()) {
            Object duration = result.get(0).get("duration");
            lastDuration = duration;
            response.put("processed", true);
            response.put("finished", false);
            response.put("duration", duration);
        } else {
            response.put("processed", true);
            response.put("finished", true);
            response.put("duration", lastDuration);
        }

        return response;
    }
}
