package com.example.eco_sistema.domain.utils;

import com.example.eco_sistema.application.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

// Validador extraído del controller para reutilización y testing
@Component
@RequiredArgsConstructor
public class ImageValidator {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final String MAX_SIZE_MESSAGE = "El archivo excede el tamaño máximo permitido de 5MB";
    private static final String EMPTY_FILE_MESSAGE = "El archivo de foto está vacío";
    private static final String INVALID_FORMAT_MESSAGE = "Solo se permiten archivos de imagen (jpg, jpeg, png)";

    private final FileStorageService fileStorageService;

    // Retorna lista de errores (vacía si es válido)
    public List<String> validate(MultipartFile file) {
        List<String> errors = new ArrayList<>();

        if (file == null || file.isEmpty()) {
            errors.add(EMPTY_FILE_MESSAGE);
            return errors;
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            errors.add(MAX_SIZE_MESSAGE);
        }

        if (!fileStorageService.isValidImageFile(file)) {
            errors.add(INVALID_FORMAT_MESSAGE);
        }

        return errors;
    }

    public boolean isValid(MultipartFile file) {
        return validate(file).isEmpty();
    }
}
