package com.example.eco_sistema.application.service;

import com.example.eco_sistema.application.ports.input.InstructorUseCase;
import com.example.eco_sistema.application.ports.ouput.InstructorRepositoryPort;
import com.example.eco_sistema.domain.exception.InvalidDataException;
import com.example.eco_sistema.domain.models.enums.InstructorStatus;
import com.example.eco_sistema.domain.models.request.InstructorRequest;
import com.example.eco_sistema.domain.models.response.InstructorResponse;
import com.example.eco_sistema.domain.utils.ImageValidator;
import com.example.eco_sistema.domain.utils.ValidationUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InstructorService implements InstructorUseCase {

    private final InstructorRepositoryPort instructorRepositoryPort;
    private final FileStorageService fileStorageService;
    private final ImageValidator imageValidator;
    private final Validator validator;

    @Override
    public void save(InstructorRequest request) {
        instructorRepositoryPort.save(request);
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public List<InstructorResponse> getList() {
        return instructorRepositoryPort.getList();
    }

    @Override
    public Page<InstructorResponse> getPagination(String search, Pageable pageable) {
        return null;
    }

    @Override
    public InstructorResponse getDomain(Long id) {
        return null;
    }

    @Override
    public void update(InstructorRequest request, Long id) {
        instructorRepositoryPort.update(request, id);
    }

    @Override
    public InstructorResponse publish(Long id) {
        return instructorRepositoryPort.publish(id);
    }

    @Override
    public InstructorResponse unpublish(Long id) {
        return instructorRepositoryPort.unpublish(id);
    }

    @Override
    public InstructorResponse deactivate(Long id) {
        return instructorRepositoryPort.deactivate(id);
    }

    // ─── Métodos de orquestración (validar → sanitizar → subir foto → persistir) ──

    public void createInstructor(MultipartFile file, InstructorRequest rawRequest) {
        validateImage(file);
        InstructorRequest sanitized = sanitize(rawRequest, InstructorStatus.active);
        validateRequest(sanitized);

        String photoPath = fileStorageService.storeFile(file, "instructors");
        InstructorRequest finalRequest = rebuildWithPhoto(sanitized, photoPath);

        instructorRepositoryPort.save(finalRequest);
    }

    public void updateInstructor(Long id, MultipartFile file, InstructorRequest rawRequest) {
        if (file != null && !file.isEmpty()) {
            validateImage(file);
        }

        InstructorRequest sanitized = sanitize(rawRequest, rawRequest.getStatus());
        validateRequest(sanitized);

        String photoPath = (file != null && !file.isEmpty())
                ? fileStorageService.storeFile(file, "instructors")
                : null;

        InstructorRequest finalRequest = rebuildWithPhoto(sanitized, photoPath);
        instructorRepositoryPort.update(finalRequest, id);
    }

    // ─── Métodos privados ───────────────────────────────────────────────────────

    private void validateImage(MultipartFile file) {
        List<String> errors = imageValidator.validate(file);
        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }

    private void validateRequest(InstructorRequest request) {
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<InstructorRequest>> violations = validator.validate(request);
        violations.forEach(v -> errors.add(v.getMessage()));
        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }

    private InstructorRequest sanitize(InstructorRequest raw, InstructorStatus defaultStatus) {
        return InstructorRequest.builder()
                .name(ValidationUtils.sanitizeString(raw.getName()))
                .last_name(ValidationUtils.sanitizeString(raw.getLast_name()))
                .specialty(ValidationUtils.sanitizeString(raw.getSpecialty()))
                .belt_level(ValidationUtils.sanitizeString(raw.getBelt_level()))
                .bio(ValidationUtils.sanitizeString(raw.getBio()))
                .email(raw.getEmail().trim().toLowerCase())
                .phone(raw.getPhone().trim())
                .experience_years(raw.getExperience_years())
                .certifications(ValidationUtils.sanitizeString(raw.getCertifications()))
                .social_media_facebook(trimOrNull(raw.getSocial_media_facebook()))
                .social_media_instagram(trimOrNull(raw.getSocial_media_instagram()))
                .social_media_twitter(trimOrNull(raw.getSocial_media_twitter()))
                .status(raw.getStatus() != null ? raw.getStatus() : defaultStatus)
                .photo("temp")
                .build();
    }

    private InstructorRequest rebuildWithPhoto(InstructorRequest base, String photoPath) {
        return InstructorRequest.builder()
                .name(base.getName())
                .last_name(base.getLast_name())
                .specialty(base.getSpecialty())
                .belt_level(base.getBelt_level())
                .bio(base.getBio())
                .email(base.getEmail())
                .phone(base.getPhone())
                .experience_years(base.getExperience_years())
                .certifications(base.getCertifications())
                .social_media_facebook(base.getSocial_media_facebook())
                .social_media_instagram(base.getSocial_media_instagram())
                .social_media_twitter(base.getSocial_media_twitter())
                .status(base.getStatus())
                .photo(photoPath)
                .build();
    }

    private String trimOrNull(String value) {
        return value != null ? value.trim() : null;
    }
}
