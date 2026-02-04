package com.example.eco_sistema.domain.exception;

import java.util.List;

public class InvalidDataException extends RuntimeException {
    private List<String> errors;

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(List<String> errors) {
        super("Errores de validación");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasMultipleErrors() {
        return errors != null && !errors.isEmpty();
    }
}
