package ru.fkr.workpetproject.controller.reports.weekly;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fkr.workpetproject.dao.dto.reports.weekly.ReportOrnWeeklyRepDto;
import ru.fkr.workpetproject.service.reports.ReportOrnWeeklyRepService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/reports/weekly/rep")
public class ReportWeeklyRepController {

    private final ReportOrnWeeklyRepService reportOrnWeeklyRepService;

    public ReportWeeklyRepController(ReportOrnWeeklyRepService service) {
        this.reportOrnWeeklyRepService = service;
    }

    @GetMapping("/")
    public List<ReportOrnWeeklyRepDto> repProjectionList(
            @RequestParam("reportDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate) {
        return reportOrnWeeklyRepService.getWeeklyRep(reportDate);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadWeeklyRep(
            @RequestParam(value = "reportDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reportDate) {

        try {
            byte[] excelBytes = reportOrnWeeklyRepService.generateXlsxReport(reportDate);

            String formattedDate = reportDate != null
                    ? reportDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    : LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String filename = String.format("rep_on_%s.xlsx", formattedDate);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(filename)
                    .build());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
