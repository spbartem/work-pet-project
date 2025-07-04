package ru.fkr.workpetproject.controller.reports.monthly;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fkr.workpetproject.service.reports.ReportOrnQuarterlyProcuracyService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/reports/quarterly/procuracy")
public class ReportQuarterlyProcuracyController {

    private final ReportOrnQuarterlyProcuracyService reportOrnQuarterlyProcuracyService;

    public ReportQuarterlyProcuracyController(ReportOrnQuarterlyProcuracyService service) {
        this.reportOrnQuarterlyProcuracyService = service;
    }

    @GetMapping("/")
    public List<Object[]> getQuarterlyProcuracyReport(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reportOrnQuarterlyProcuracyService.getQuarterlyProcuracyCollectability(date);
    }

    @GetMapping("/dates")
    public List<LocalDate> getAvailableReportDates() {
        return reportOrnQuarterlyProcuracyService.getAvailableReportDates();
    }

    @GetMapping(value = "/download", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> downloadQuarterlyProcuracyReport(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws IOException {
        List<Object[]> dataCollectability = reportOrnQuarterlyProcuracyService.getQuarterlyProcuracyCollectability(date);
        List<Object[]> dataDebt = reportOrnQuarterlyProcuracyService.getQuarterlyProcuracyDebt(date);
        byte[] xlsxBytes = reportOrnQuarterlyProcuracyService.generateXlsxReport(dataCollectability, dataDebt, date);

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.ok()
                .headers(headers)
                .body(xlsxBytes);
    }

    @PostMapping("/fill")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> fillReport() {
        return reportOrnQuarterlyProcuracyService.fillReport()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/check-fill")
    public ResponseEntity<Map<String, Object>> checkFillAvailability() {
        Map<String, Object> availability = reportOrnQuarterlyProcuracyService.checkFillAvailability();
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/check-progress-status")
    public ResponseEntity<Map<String, Object>> checkProgressStatus() {
        Map<String, Object> status = reportOrnQuarterlyProcuracyService.checkProgressStatusReportOrnQuarterlyCollectability();
        return ResponseEntity.ok(status);
    }

    @GetMapping("/fill-duration")
    public ResponseEntity<Long> getFillDuration() {
        return ResponseEntity.ok(reportOrnQuarterlyProcuracyService.getLastDurationMillis());
    }
}
