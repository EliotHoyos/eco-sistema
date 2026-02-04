package com.example.eco_sistema.application.service;

import com.example.eco_sistema.application.service.FileStorageService;
import com.example.eco_sistema.domain.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

// Almacenamiento local con nombres UUID para evitar colisiones
@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png"};

    @Override
    public String storeFile(MultipartFile file, String folder) {
        if (file.isEmpty()) {
            throw new FileStorageException("El archivo está vacío");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileStorageException("El archivo excede el tamaño máximo permitido de 5MB");
        }

        if (!isValidImageFile(file)) {
            throw new FileStorageException("Solo se permiten archivos de imagen (jpg, jpeg, png)");
        }

        try {
            String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileExtension = getFileExtension(originalFilename);
            String newFileName = UUID.randomUUID() + "." + fileExtension;

            Path uploadPath = Paths.get(uploadDir, folder);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path targetLocation = uploadPath.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Archivo guardado exitosamente: {}", newFileName);

            return folder + "/" + newFileName;

        } catch (IOException ex) {
            throw new FileStorageException("No se pudo almacenar el archivo. Intente nuevamente.", ex);
        }
    }

    @Override
    public void deleteFile(String fileName, String folder) {
        try {
            Path filePath = Paths.get(uploadDir, folder, fileName);
            Files.deleteIfExists(filePath);
            log.info("Archivo eliminado: {}", fileName);
        } catch (IOException ex) {
            log.error("Error al eliminar el archivo: {}", fileName, ex);
        }
    }

    @Override
    public boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }

        if (!contentType.startsWith("image/")) {
            return false;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }

        String extension = getFileExtension(originalFilename).toLowerCase();

        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equals(extension)) {
                return true;
            }
        }

        return false;
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
}
