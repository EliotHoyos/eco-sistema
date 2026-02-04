package com.example.eco_sistema.infrastructure.advice;

import com.example.eco_sistema.domain.exception.DuplicateResourceException;
import com.example.eco_sistema.domain.exception.FileStorageException;
import com.example.eco_sistema.domain.exception.InvalidDataException;
import com.example.eco_sistema.domain.exception.ResourceNotFoundException;
import com.example.eco_sistema.domain.models.dto.ErrorDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDto> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = "El parámetro '" + ex.getParameterName() + "' es obligatorio";

        ErrorDto errorDto = ErrorDto.builder()
                .errores(List.of(message))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            errors.add(error.getDefaultMessage());
        });

        ErrorDto errorDto = ErrorDto.builder()
                .errores(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errores(List.of(ex.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorDto> handleInvalidDataException(InvalidDataException ex) {
        List<String> errors = ex.hasMultipleErrors()
            ? ex.getErrors()
            : List.of(ex.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .errores(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorDto> handleFileStorageException(FileStorageException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errores(List.of(ex.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorDto> handleDuplicateResourceException(DuplicateResourceException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errores(List.of(ex.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "Ya existe un registro con los datos proporcionados";

        if (ex.getMessage().contains("document")) {
            message = "Ya existe un cliente con ese documento";
        } else if (ex.getMessage().contains("email")) {
            message = "Ya existe un cliente con ese email";
        }

        ErrorDto errorDto = ErrorDto.builder()
                .errores(List.of(message))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGlobalException(Exception ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errores(List.of("Ha ocurrido un error interno en el servidor"))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
