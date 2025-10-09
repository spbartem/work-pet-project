package ru.fkr.workpetproject.utils;

import org.apache.poi.ss.usermodel.*;

public class ExcelUtils {
    /**
     * Скопировать стиль ячейки.
     */
    public static void copyCellStyleOnly(Cell sourceCell, Cell targetCell) {
        if (sourceCell == null || targetCell == null) {
            return;
        }

        Workbook targetWorkbook = targetCell.getSheet().getWorkbook(); // Используем Workbook target'а
        CellStyle sourceStyle = sourceCell.getCellStyle();

        CellStyle newStyle = targetWorkbook.createCellStyle();
        newStyle.cloneStyleFrom(sourceStyle);

        targetCell.setCellStyle(newStyle);
    }


    /**
     * Скопировать ячейку целиком.
     */
    public static void copyCellFully(Cell sourceCell, Cell targetCell) {
        copyCellStyleOnly(sourceCell, targetCell);

        if (sourceCell == null || targetCell == null) {
            return;
        }

        switch (sourceCell.getCellType()) {
            case STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(sourceCell)) {
                    targetCell.setCellValue(sourceCell.getDateCellValue());
                } else {
                    targetCell.setCellValue(sourceCell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                targetCell.setCellFormula(sourceCell.getCellFormula());
                break;
            case BLANK:
                targetCell.setBlank();
                break;
            default:
                break;
        }
    }

    /**
     * Безопасно получить или создать строку в листе.
     */
    public static Row getOrCreateRow(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        return (row == null) ? sheet.createRow(rowIndex) : row;
    }

    /**
     * Безопасно получить или создать ячейку в строке.
     */
    public static Cell getOrCreateCell(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        return (cell == null) ? row.createCell(colIndex) : cell;
    }

    /**
     * Безопасная вставка значения в ячейку Excel по индексу строки и столбца.
     */
    public static void setCellValueSafe(Sheet sheet, int rowIndex, int colIndex, Object value) {
        Row row = getOrCreateRow(sheet, rowIndex);
        Cell cell = getOrCreateCell(row, colIndex);

        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof java.util.Date) {
            cell.setCellValue((java.util.Date) value);
        } else if (value == null) {
            cell.setBlank();
        } else {
            // Всё остальное конвертируем в строку
            cell.setCellValue(value.toString());
        }
    }
}

