package com.pharma.utils;

public class CsvUtils {

    private CsvUtils() {}

    public static String normalize(String value) {
        if (value == null) return "";
        return value
                .trim()
                .toLowerCase()
                .replace("\uFEFF", "")   // BOM
                .replaceAll("\\s+", " ");
    }
}
