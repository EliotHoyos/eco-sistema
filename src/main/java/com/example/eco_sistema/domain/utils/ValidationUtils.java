package com.example.eco_sistema.domain.utils;

public class ValidationUtils {

    private ValidationUtils() {
        throw new IllegalStateException("Utility class");
    }

    // Normaliza espacios: trim + colapsa múltiples espacios en uno
    public static String sanitizeString(String input) {
        if (input == null) {
            return null;
        }
        return input.trim().replaceAll("\\s+", " ");
    }
}
