package ru.fkr.workpetproject.service.reports;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import ru.fkr.workpetproject.repository.reports.ReportOrnQuarterlyProcuracyRepository;
import ru.fkr.workpetproject.utils.CellStyleHelper;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
public class ReportOrnQuarterlyProcuracyService {

    @Autowired
    private ReportOrnQuarterlyProcuracyRepository reportOrnQuarterlyProcuracyRepository;
    private long lastDurationMillis = -1;

    public List<Object[]> getQuarterlyProcuracyCollectability(LocalDate date) {
        return reportOrnQuarterlyProcuracyRepository.getQuarterlyProcuracyCollectability(date);
    }

    public List<Object[]> getQuarterlyProcuracyDebt(LocalDate date) {
        return reportOrnQuarterlyProcuracyRepository.getQuarterlyProcuracyDebt(date);
    }

    public List<LocalDate> getAvailableReportDates() {
        return reportOrnQuarterlyProcuracyRepository.getAvailableReportDates();
    }

    public Map<String, Object> checkFillAvailability() {
        return reportOrnQuarterlyProcuracyRepository.checkFillAvailability();
    }

    public Map<String, Object> checkProgressStatusReportOrnQuarterlyCollectability() {
        return reportOrnQuarterlyProcuracyRepository.checkProgressStatusReportOrnQuarterlyProcuracy();
    }

    @Async
    public CompletableFuture<Map<String, Object>> fillReport() {
        Instant start = Instant.now();
        try {
            Map<String, Object> result = reportOrnQuarterlyProcuracyRepository.fillReport();
            return CompletableFuture.completedFuture(result);
        } finally {
            Instant end = Instant.now();
            lastDurationMillis = Duration.between(start, end).toMillis();
        }
    }

    public long getLastDurationMillis() {
        return lastDurationMillis;
    }
    public byte[] generateXlsxReport(List<Object[]> dataCollectability, List<Object[]> dataDebt, LocalDate date) throws IOException {

        DateTimeFormatter formatterMonthYear = DateTimeFormatter.ofPattern("LLLL yyyy", new Locale("ru"));
        DateTimeFormatter formatterDayMonthYear = DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("ru"));

        String reportYear = String.valueOf(date.getYear());
        String prevYear = String.valueOf(date.getYear() - 1);
        String dateCondition;

        if (date != date.withDayOfMonth(date.lengthOfMonth())) {
            dateCondition = date.format(formatterDayMonthYear);
        } else {
            dateCondition = date.plusDays(1).format(formatterDayMonthYear);
        }
        String formattedDateFees = date.minusMonths(1).format(formatterMonthYear);
        String formattedDatePayments = date.format(formatterMonthYear);

        try (InputStream inputStream = new ClassPathResource("templates/reports/quarterly/collectability_template.xlsx").getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream)) {

            //Лист 1 - Собираемость
            Sheet sheetCollectability = workbook.getSheetAt(0);

            //Заполнение переменных в шапке шаблона
            for (Row row : sheetCollectability) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        String cellValue = cell.getStringCellValue();

                        // Проверяем, содержит ли ячейка какую-либо переменную
                        if (cellValue.contains("{{CONDITION_DATE}}")
                                || cellValue.contains("{{PREV_YEAR}}")
                                || cellValue.contains("{{CUR_YEAR}}")
                                || cellValue.contains("{{DATE_FEES}}")
                                || cellValue.contains("{{DATE_PAYMENTS}}")) {
                            switch (cellValue) {
                                case "{{CONDITION_DATE}}":
                                    cell.setCellValue(dateCondition);
                                    break;
                                case "{{PREV_YEAR}}":
                                    cell.setCellValue(prevYear);
                                    break;
                                case "{{CUR_YEAR}}":
                                    cell.setCellValue(reportYear);
                                    break;
                                case "{{DATE_FEES}}":
                                    cell.setCellValue(formattedDateFees);
                                    break;
                                case "{{DATE_PAYMENTS}}":
                                    cell.setCellValue(formattedDatePayments);
                                    break;
                                default:
                                    // Если в ячейке есть несколько переменных, заменяем их по очереди
                                    cellValue = cellValue.replace("{{CONDITION_DATE}}", dateCondition)
                                            .replace("{{PREV_YEAR}}", prevYear)
                                            .replace("{{CUR_YEAR}}", reportYear)
                                            .replace("{{DATE_FEES}}", formattedDateFees)
                                            .replace("{{DATE_PAYMENTS}}", formattedDatePayments);
                                    cell.setCellValue(cellValue);
                                    break;
                            }
                        }
                    }
                }
            }

            CellStyle numberStyle = CellStyleHelper.createNumberStyle(workbook);

            int rowNum = 4;
            for (Object[] rowDataCollectability : dataCollectability) {
                Row dataRow = sheetCollectability.createRow(rowNum++);
                dataRow.createCell(0).setCellValue(((String) rowDataCollectability[0]).replace(" район", "")); // districtName
                dataRow.createCell(1).setCellValue((String) rowDataCollectability[1]); // houseFullAddress

                // Заполняем числа
                for (int i = 2; i < rowDataCollectability.length; i++) {
                    Cell cell = dataRow.createCell(i);
                    if (rowDataCollectability[i] instanceof Number) {
                        cell.setCellValue(((Number) rowDataCollectability[i]).doubleValue());
                        cell.setCellStyle(numberStyle);
                    } else {
                        String value;
                        if (rowDataCollectability[i] != null) {
                            value = rowDataCollectability[i].toString();
                        } else {
                            value = "N/A";
                        }
                        cell.setCellValue(value);
                    }
                }

                CellStyle percentageStyle = CellStyleHelper.createPercentageStyle(workbook);

                // Вычисляем собираемость и записываем результат в новый столбец
                if (rowDataCollectability[2] instanceof Number && rowDataCollectability[3] instanceof Number) {
                    double numerator = ((Number) rowDataCollectability[3]).doubleValue();
                    double denominator = ((Number) rowDataCollectability[2]).doubleValue();

                    Cell resultCell = dataRow.createCell(rowDataCollectability.length); // Следующий столбец
                    if (denominator != 0) {
                        resultCell.setCellValue(numerator / denominator);
                    } else {
                        resultCell.setCellValue("-");
                    }
                    resultCell.setCellStyle(percentageStyle);
                }
            }

            //Лист 2 - Задолженность
            Sheet sheetDebt = workbook.getSheetAt(1);

            CellStyle dateStyle = CellStyleHelper.createDateStyle(workbook);
            rowNum = 4;

            for (Object[] rowData : dataDebt) {
                Row dataRow = sheetDebt.createRow(rowNum++);
                dataRow.createCell(0).setCellValue((Date) rowData[0]);
                dataRow.getCell(0).setCellStyle(dateStyle);
                dataRow.createCell(1).setCellValue(((Number) rowData[1]).doubleValue());
                dataRow.getCell(1).setCellStyle(numberStyle);
                dataRow.createCell(2).setCellValue(((Number) rowData[2]).doubleValue());
                dataRow.getCell(2).setCellStyle(numberStyle);
            }

            // Запись в ByteArrayOutputStream
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }
}
