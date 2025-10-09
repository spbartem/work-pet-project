package ru.fkr.workpetproject.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {

    private static final NumberFormat RUS_FORMATTER =
            NumberFormat.getInstance(Locale.forLanguageTag("ru-RU"));

    private NumberUtils() {
        // утилитный класс — приватный конструктор
    }

    public static String formatNumber(Integer value) {
        return value == null ? "0" : RUS_FORMATTER.format(value);
    }

    public static String formatNumber(Long value) {
        return value == null ? "0" : RUS_FORMATTER.format(value);
    }
}

