package com.example.eco_sistema.application.service;

import com.example.eco_sistema.application.ports.input.ClientUseCase;
import com.example.eco_sistema.application.ports.ouput.ClientRepositoryPort;
import com.example.eco_sistema.domain.exception.InvalidDataException;
import com.example.eco_sistema.domain.models.request.ClientRequest;
import com.example.eco_sistema.domain.models.response.ClientResponse;
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
public class ClientService implements ClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;
    private final FileStorageService fileStorageService;
    private final ImageValidator imageValidator;
    private final Validator validator;

    @Override
    public void save(ClientRequest request) {
        clientRepositoryPort.save(request);
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public List<ClientResponse> getList() {
        return clientRepositoryPort.getList();
    }

    @Override
    public Page<ClientResponse> getPagination(String search, Pageable pageable) {
        return null;
    }

    @Override
    public ClientResponse getDomain(Long id) {
        return null;
    }

    @Override
    public void update(ClientRequest request, Long id) {
    }

    // ─── Orquestración: validar → sanitizar → subir foto → persistir ──────────

    public void createClient(MultipartFile file, ClientRequest rawRequest) {
        validateImage(file);
        ClientRequest sanitized = sanitize(rawRequest);
        validateRequest(sanitized);

        String photoPath = fileStorageService.storeFile(file, "clients");
        ClientRequest finalRequest = rebuildWithPhoto(sanitized, photoPath);

        clientRepositoryPort.save(finalRequest);
    }

    // ─── Métodos privados ───────────────────────────────────────────────────────

    private void validateImage(MultipartFile file) {
        List<String> errors = imageValidator.validate(file);
        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }

    private void validateRequest(ClientRequest request) {
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<ClientRequest>> violations = validator.validate(request);
        violations.forEach(v -> errors.add(v.getMessage()));
        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }

    private ClientRequest sanitize(ClientRequest raw) {
        return ClientRequest.builder()
                .name(ValidationUtils.sanitizeString(raw.getName()))
                .last_name(ValidationUtils.sanitizeString(raw.getLast_name()))
                .document_type(raw.getDocument_type())
                .document(raw.getDocument() != null ? raw.getDocument().replaceAll("[^0-9]", "") : null)
                .address(ValidationUtils.sanitizeString(raw.getAddress()))
                .cellphome(raw.getCellphome() != null ? raw.getCellphome().trim() : null)
                .email(raw.getEmail().trim().toLowerCase())
                .gender(raw.getGender())
                .birthday(raw.getBirthday())
                .photo("temp")
                .build();
    }

    private ClientRequest rebuildWithPhoto(ClientRequest base, String photoPath) {
        return ClientRequest.builder()
                .name(base.getName())
                .last_name(base.getLast_name())
                .document_type(base.getDocument_type())
                .document(base.getDocument())
                .address(base.getAddress())
                .cellphome(base.getCellphome())
                .email(base.getEmail())
                .gender(base.getGender())
                .birthday(base.getBirthday())
                .photo(photoPath)
                .build();
    }
}
