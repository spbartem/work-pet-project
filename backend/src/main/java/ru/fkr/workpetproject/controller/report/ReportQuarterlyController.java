package ru.fkr.workpetproject.controller.report;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fkr.workpetproject.service.report.ReportOrnQuarterlyCollectabilityService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/reports/quarterly")
public class ReportQuarterlyController {

    private final ReportOrnQuarterlyCollectabilityService reportOrnQuarterlyCollectabilityService;

    public ReportQuarterlyController(ReportOrnQuarterlyCollectabilityService service) {
        this.reportOrnQuarterlyCollectabilityService = service;
    }

    @GetMapping("/collectability")
    public List<Object[]> getQuarterlyCollectabilityReport(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reportOrnQuarterlyCollectabilityService.getQuarterlyProcuracyCollectability(date);
    }

    @GetMapping("/collectability/dates")
    public List<LocalDate> getAvailableReportDates() {
        return reportOrnQuarterlyCollectabilityService.getAvailableReportDates();
    }

    @GetMapping(value = "/collectability/download", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> downloadQuarterlyProcuracyReport(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws IOException {
        List<Object[]> dataCollectability = reportOrnQuarterlyCollectabilityService.getQuarterlyProcuracyCollectability(date);
        List<Object[]> dataDebt = reportOrnQuarterlyCollectabilityService.getQuarterlyProcuracyDebt(date);
        byte[] xlsxBytes = reportOrnQuarterlyCollectabilityService.generateXlsxReport(dataCollectability, dataDebt, date);

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.ok()
                .headers(headers)
                .body(xlsxBytes);
    }

    @PostMapping("/fill")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> fillReport() {
        return reportOrnQuarterlyCollectabilityService.fillReport()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/collectability/check-fill")
    public ResponseEntity<Map<String, Object>> checkFillAvailability() {
        Map<String, Object> availability = reportOrnQuarterlyCollectabilityService.checkFillAvailability();
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/collectability/check-progress-status")
    public ResponseEntity<Map<String, Object>> checkProgressStatus() {
        Map<String, Object> status = reportOrnQuarterlyCollectabilityService.checkProgressStatusReportOrnQuarterlyCollectability();
        return ResponseEntity.ok(status);
    }

    @GetMapping("/collectability/fill-duration")
    public ResponseEntity<Long> getFillDuration() {
        return ResponseEntity.ok(reportOrnQuarterlyCollectabilityService.getLastDurationMillis());
    }
}
