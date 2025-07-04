package ru.fkr.workpetproject.service.reports;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.fkr.workpetproject.dao.dto.reports.weekly.ReportOrnWeeklyRepDto;
import ru.fkr.workpetproject.repository.reports.ReportOrnWeeklyRepRepository;
import ru.fkr.workpetproject.utils.ExcelUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

@Service
public class ReportOrnWeeklyRepService {

    private final ReportOrnWeeklyRepRepository reportOrnWeeklyRepRepository;

    public ReportOrnWeeklyRepService(ReportOrnWeeklyRepRepository reportOrnWeeklyRepRepository) {
        this.reportOrnWeeklyRepRepository = reportOrnWeeklyRepRepository;
    }

    private static final Map<String, String> reportTypeToTableName = Map.ofEntries(
            Map.entry("total_debt", "Совокупная задолженность"),
            Map.entry("debt_ul_by_sum", "Юридические лица по сумме ДЗ"),
            Map.entry("debt_ul_by_start_debt", "Юридические лица по глубине ДЗ"),
            Map.entry("debt_ul_by_period", "Юридические лица по отнесению ДЗ"),
            Map.entry("debt_fiz_by_sum", "Физические лица по сумме ДЗ"),
            Map.entry("debt_fiz_by_start_debt", "Физические лица по глубине ДЗ"),
            Map.entry("debt_fiz_by_period", "Физические лица по отнесению ДЗ"),
            Map.entry("debt_fiz_more_75000", "Физические лица больше 75 000"),
            Map.entry("debt_fiz_by_start_debt_more_75000", "Физические лица больше 75 000 (возникновение)"),
            Map.entry("debt_fiz_by_period_more_75000", "Физические лица больше 75 000 (отнесение)"),
            Map.entry("debt_fiz_by_start_debt_more_3_y", "Физические лица больше 3_х лет (возникновение)"),
            Map.entry("debt_fiz_by_period_more_3_y", "Физические лица больше 3_х лет (отнесение)")
    );

    public List<ReportOrnWeeklyRepDto> getWeeklyRep(LocalDate reportDate) {
        return reportOrnWeeklyRepRepository.getWeeklyRep(reportDate);
    }

    public byte[] generateXlsxReport(LocalDate reportDate) throws Exception {

        List<ReportOrnWeeklyRepDto> dataWeeklyRep = getWeeklyRep(reportDate);

        try(InputStream inputStream = new ClassPathResource("templates/reports/weekly/rep_template.xlsx").getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheetSummary = workbook.getSheetAt(0);
            Sheet sheetRo = workbook.getSheetAt(1); //Лист 2 - Счёт РО
            Sheet sheetSpecRo = workbook.getSheetAt(2); //Лист 3 - Спец. счёт РО
            Sheet sheetWithTemplates = workbook.getSheetAt(3); // Лист 4 - лист с шаблонами

            Cell templateCellText = sheetWithTemplates.getRow(0).getCell(1);
            Cell templateCellNumber = sheetWithTemplates.getRow(1).getCell(1);
            Cell templateCellDouble = sheetWithTemplates.getRow(2).getCell(1);
            Cell templateCellDate = sheetWithTemplates.getRow(3).getCell(1);

            Map<String, Integer> nextRowMapSheetRo = new HashMap<>();
            Map<String, Integer> nextRowMapSheetSpec = new HashMap<>();
            Map<GroupKey, AggregatedData> summaryMap = new HashMap<>();

            Set<String> typesWithDateInFirstColumn = Set.of("total_debt", "debt_fiz_more_75000", "debt_fiz_by_start_debt_more_3_y", "debt_fiz_by_period_more_3_y");

            String firstColumn;

            for (ReportOrnWeeklyRepDto dto : dataWeeklyRep) {
                String reportType = dto.getReportType();
                String tableName = reportTypeToTableName.get(reportType);
                if (tableName == null) continue;

                Sheet targetSheet = dto.getDecisionId() == 1 ? sheetRo : sheetSpecRo;
                Map<String, Integer> currentRowMap = dto.getDecisionId() == 1 ? nextRowMapSheetRo : nextRowMapSheetSpec;

                // Найдём стартовую ячейку (с заголовком)
                CellPosition position = findTableStartCell(targetSheet, tableName);
                if (position == null) continue;

                int insertRow = currentRowMap.getOrDefault(reportType, position.rowIndex + 2);
                Row row = ExcelUtils.getOrCreateRow(targetSheet, insertRow);

                // Пишем строго начиная со столбца, где была метка таблицы
                int col = position.colIndex;

                Cell cell1 = row.createCell(col);
                if (typesWithDateInFirstColumn.contains(reportType)) {
                    firstColumn = String.valueOf(dto.getReportDate());
                    cell1.setCellValue(dto.getReportDate());
                    ExcelUtils.copyCellStyleOnly(templateCellDate, cell1);
                } else {
                    firstColumn = dto.getDivision();
                    cell1.setCellValue(dto.getDivision());
                    ExcelUtils.copyCellStyleOnly(templateCellText, cell1);
                }

                Cell cell2 = row.createCell(col + 1);
                cell2.setCellValue(dto.getLsCount());
                ExcelUtils.copyCellStyleOnly(templateCellNumber, cell2);

                Cell cell3 = row.createCell(col + 2);
                cell3.setCellValue(dto.getTotalAmount().doubleValue());
                ExcelUtils.copyCellStyleOnly(templateCellDouble, cell3);

                Cell cell4 = row.createCell(col + 3);
                if (dto.getInnCount().equals(0)) {
                    cell4.setCellValue("");
                } else {
                    cell4.setCellValue(dto.getInnCount());
                    ExcelUtils.copyCellStyleOnly(templateCellNumber, cell4);
                }

                // Сохраняем индекс следующей строки для этого reportType
                currentRowMap.put(reportType, insertRow + 1);

                // Собираем данные для листа Итоги
                GroupKey key = new GroupKey(firstColumn, dto.getReportType());
                AggregatedData aggData = summaryMap.getOrDefault(key, new AggregatedData());
                if (dto.getDecisionId() == 1) {
                    aggData.add(dto.getLsCount(), dto.getTotalAmount().doubleValue(), dto.getInnCount(), new CellPosition(insertRow, col));
                } else {
                    aggData.add(dto.getLsCount(), dto.getTotalAmount().doubleValue(), dto.getInnCount());
                }

                summaryMap.put(key, aggData);
            }

            for (Map.Entry<GroupKey, AggregatedData> entry: summaryMap.entrySet()) {

                GroupKey groupKey = entry.getKey();
                AggregatedData aggregatedData = entry.getValue();
                int rowIndex = aggregatedData.position.rowIndex;
                int colIndex = aggregatedData.position.colIndex;

                Cell cell1 = sheetSummary.getRow(rowIndex).getCell(colIndex);
                if (typesWithDateInFirstColumn.contains(groupKey.reportType)) {
                    LocalDate localDate = LocalDate.parse(groupKey.division);
                    Date date = java.sql.Date.valueOf(localDate);
                    ExcelUtils.setCellValueSafe(sheetSummary, rowIndex, colIndex, date);
                    ExcelUtils.copyCellStyleOnly(templateCellDate, cell1);
                } else {ExcelUtils.setCellValueSafe(sheetSummary, rowIndex, colIndex, groupKey.division);
                    ExcelUtils.copyCellStyleOnly(templateCellText, cell1);
                }

                ExcelUtils.setCellValueSafe(sheetSummary, rowIndex, colIndex + 1, aggregatedData.lsCountSum);
                Cell cell2 = sheetSummary.getRow(rowIndex).getCell(colIndex + 1);
                ExcelUtils.copyCellStyleOnly(templateCellNumber, cell2);

                ExcelUtils.setCellValueSafe(sheetSummary, rowIndex, colIndex + 2, aggregatedData.totalAmountSum);
                Cell cell3 = sheetSummary.getRow(rowIndex).getCell(colIndex + 2);
                ExcelUtils.copyCellStyleOnly(templateCellDouble, cell3);

                if (aggregatedData.innCountSum == 0) {
                    ExcelUtils.setCellValueSafe(sheetSummary, rowIndex, colIndex + 3, "");
                } else {
                    ExcelUtils.setCellValueSafe(sheetSummary, rowIndex, colIndex + 3, aggregatedData.innCountSum);
                    Cell cell4 = sheetSummary.getRow(rowIndex).getCell(colIndex + 3);
                    ExcelUtils.copyCellStyleOnly(templateCellNumber, cell4);
                }
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.setForceFormulaRecalculation(true);
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    private CellPosition findTableStartCell(Sheet sheet, String tableName) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            for (int j = 0; j <= 10; j++) { // столбцы A (0) до K (10)
                Cell cell = row.getCell(j);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue().trim().equalsIgnoreCase(tableName.trim())) {
                        return new CellPosition(i, j);
                    }
                }
            }
        }
        return null;
    }

    public record CellPosition(int rowIndex, int colIndex) {
    }

    // Класс для хранения агрегированных данных
    public static class AggregatedData {
        int lsCountSum;
        double totalAmountSum;
        int innCountSum;
        CellPosition position;

        public void add(int lsCount, double totalAmount, int innCount, CellPosition position) {
            this.lsCountSum += lsCount;
            this.totalAmountSum += totalAmount;
            this.innCountSum += innCount;
            this.position = position;
        }

        public void add(int lsCount, double totalAmount, int innCount) {
            this.lsCountSum += lsCount;
            this.totalAmountSum += totalAmount;
            this.innCountSum += innCount;
        }
    }

    // Ключ для группировки
    public static class GroupKey {
        String division;
        String reportType;

        public GroupKey(String division, String reportType) {
            this.division = division;
            this.reportType = reportType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GroupKey groupKey = (GroupKey) o;

            return Objects.equals(division, groupKey.division) && Objects.equals(reportType, groupKey.reportType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(division, reportType);
        }
    }
}
