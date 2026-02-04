package com.example.eco_sistema.application.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file, String folder);
    void deleteFile(String fileName, String folder);
    boolean isValidImageFile(MultipartFile file);
}
