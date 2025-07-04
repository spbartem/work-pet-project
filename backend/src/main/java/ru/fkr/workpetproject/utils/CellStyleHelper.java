package ru.fkr.workpetproject.utils;

import org.apache.poi.ss.usermodel.*;

public class CellStyleHelper {

    //формат числа с двумя знаками после запятой
    public static CellStyle createNumberStyle(Workbook workbook) {
        CellStyle numberStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        numberStyle.setDataFormat(format.getFormat("#,##0.00"));
        return numberStyle;
    }

    //формат в проценты с двумя знаками после запятой
    public static CellStyle createPercentageStyle(Workbook workbook) {
        CellStyle percentageStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        percentageStyle.setDataFormat(format.getFormat("0.00%"));
        return percentageStyle;
    }

    public static CellStyle createDateStyle(Workbook workbook) {
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy"));
        return dateStyle;
    }
}

