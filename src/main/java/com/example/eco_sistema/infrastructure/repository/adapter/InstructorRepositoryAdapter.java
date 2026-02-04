package com.example.eco_sistema.infrastructure.repository.adapter;

import com.example.eco_sistema.application.ports.ouput.InstructorRepositoryPort;
import com.example.eco_sistema.domain.exception.ResourceNotFoundException;
import com.example.eco_sistema.domain.models.request.InstructorRequest;
import com.example.eco_sistema.domain.models.response.InstructorResponse;
import com.example.eco_sistema.infrastructure.repository.entities.InstructorEntity;
import com.example.eco_sistema.domain.models.enums.InstructorStatus;
import com.example.eco_sistema.infrastructure.repository.jpa.InstructorJpaRepository;
import com.example.eco_sistema.infrastructure.repository.mapper.InstructorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InstructorRepositoryAdapter implements InstructorRepositoryPort {

    private final InstructorJpaRepository instructorJpaRepository;
    private final InstructorMapper instructorMapper;

    @Override
    public void save(InstructorRequest request) {
        InstructorEntity entity = instructorMapper.toEntity(request);
        instructorJpaRepository.save(entity);
    }

    @Override
    public Object saveEntity(InstructorRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<InstructorResponse> getList() {
        List<InstructorEntity> entities = instructorJpaRepository.findAll();
        return instructorMapper.toResponseList(entities);
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
        InstructorEntity entity = instructorJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor no encontrado con id: " + id));

        entity.setName(request.getName());
        entity.setLast_name(request.getLast_name());
        entity.setSpecialty(request.getSpecialty());
        entity.setBelt_level(request.getBelt_level());
        entity.setBio(request.getBio());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setExperience_years(request.getExperience_years());
        entity.setCertifications(request.getCertifications());
        entity.setSocial_media_facebook(request.getSocial_media_facebook());
        entity.setSocial_media_instagram(request.getSocial_media_instagram());
        entity.setSocial_media_twitter(request.getSocial_media_twitter());
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }
        // Solo actualiza la foto si se proporcionó una nueva
        if (request.getPhoto() != null && !request.getPhoto().isBlank()) {
            entity.setPhoto(request.getPhoto());
        }

        instructorJpaRepository.save(entity);
    }

    @Override
    public InstructorResponse publish(Long id) {
        InstructorEntity entity = instructorJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor no encontrado con id: " + id));
        entity.setIsPublished(true);
        InstructorEntity savedEntity = instructorJpaRepository.save(entity);
        return instructorMapper.toResponse(savedEntity);
    }

    @Override
    public InstructorResponse unpublish(Long id) {
        InstructorEntity entity = instructorJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor no encontrado con id: " + id));
        entity.setIsPublished(false);
        InstructorEntity savedEntity = instructorJpaRepository.save(entity);
        return instructorMapper.toResponse(savedEntity);
    }

    @Override
    public InstructorResponse deactivate(Long id) {
        InstructorEntity entity = instructorJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor no encontrado con id: " + id));
        entity.setStatus(InstructorStatus.inactive);
        InstructorEntity savedEntity = instructorJpaRepository.save(entity);
        return instructorMapper.toResponse(savedEntity);
    }
}
