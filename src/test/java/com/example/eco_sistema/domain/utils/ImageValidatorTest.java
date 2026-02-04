package com.example.eco_sistema.domain.utils;

import com.example.eco_sistema.application.service.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageValidatorTest {

    @Mock
    private FileStorageService fileStorageService;

    private ImageValidator imageValidator;

    @BeforeEach
    void setUp() {
        imageValidator = new ImageValidator(fileStorageService);
    }

    @Test
    void shouldReturnErrorWhenFileIsEmpty() {
        MultipartFile emptyFile = new MockMultipartFile("photo", "test.jpg", "image/jpeg", new byte[0]);

        List<String> errors = imageValidator.validate(emptyFile);

        assertFalse(errors.isEmpty());
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("vacío"));
        verifyNoInteractions(fileStorageService);
    }

    @Test
    void shouldReturnErrorWhenFileIsNull() {
        List<String> errors = imageValidator.validate(null);

        assertFalse(errors.isEmpty());
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("vacío"));
        verifyNoInteractions(fileStorageService);
    }

    @Test
    void shouldReturnErrorWhenFileSizeExceedsMaximum() {
        byte[] largeContent = new byte[6 * 1024 * 1024];
        MultipartFile largeFile = new MockMultipartFile("photo", "large.jpg", "image/jpeg", largeContent);
        when(fileStorageService.isValidImageFile(largeFile)).thenReturn(true);

        List<String> errors = imageValidator.validate(largeFile);

        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(error -> error.contains("5MB")));
    }

    @Test
    void shouldReturnErrorWhenFormatIsInvalid() {
        byte[] content = new byte[1024];
        MultipartFile invalidFile = new MockMultipartFile("photo", "document.pdf", "application/pdf", content);
        when(fileStorageService.isValidImageFile(invalidFile)).thenReturn(false);

        List<String> errors = imageValidator.validate(invalidFile);

        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(error -> error.contains("imagen")));
        verify(fileStorageService, times(1)).isValidImageFile(invalidFile);
    }

    @Test
    void shouldReturnMultipleErrorsWhenMultipleIssuesExist() {
        byte[] largeContent = new byte[6 * 1024 * 1024];
        MultipartFile invalidFile = new MockMultipartFile("photo", "large.pdf", "application/pdf", largeContent);
        when(fileStorageService.isValidImageFile(invalidFile)).thenReturn(false);

        List<String> errors = imageValidator.validate(invalidFile);

        assertEquals(2, errors.size());
        assertTrue(errors.stream().anyMatch(error -> error.contains("5MB")));
        assertTrue(errors.stream().anyMatch(error -> error.contains("imagen")));
    }

    @Test
    void shouldValidateValidFile() {
        byte[] validContent = new byte[2 * 1024 * 1024];
        MultipartFile validFile = new MockMultipartFile("photo", "valid.jpg", "image/jpeg", validContent);
        when(fileStorageService.isValidImageFile(validFile)).thenReturn(true);

        List<String> errors = imageValidator.validate(validFile);

        assertTrue(errors.isEmpty());
        verify(fileStorageService, times(1)).isValidImageFile(validFile);
    }

    @Test
    void isValidShouldReturnTrueForValidFile() {
        byte[] validContent = new byte[1024 * 1024];
        MultipartFile validFile = new MockMultipartFile("photo", "valid.png", "image/png", validContent);
        when(fileStorageService.isValidImageFile(validFile)).thenReturn(true);

        boolean isValid = imageValidator.isValid(validFile);

        assertTrue(isValid);
    }

    @Test
    void isValidShouldReturnFalseForInvalidFile() {
        MultipartFile emptyFile = new MockMultipartFile("photo", "empty.jpg", "image/jpeg", new byte[0]);

        boolean isValid = imageValidator.isValid(emptyFile);

        assertFalse(isValid);
    }
}
