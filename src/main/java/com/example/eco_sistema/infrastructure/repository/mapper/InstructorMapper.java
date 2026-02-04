package com.example.eco_sistema.infrastructure.repository.mapper;

import com.example.eco_sistema.domain.models.request.InstructorRequest;
import com.example.eco_sistema.domain.models.response.InstructorResponse;
import com.example.eco_sistema.infrastructure.repository.entities.InstructorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    InstructorEntity toEntity(InstructorRequest request);

    @Mapping(source = "isPublished", target = "is_published")
    InstructorResponse toResponse(InstructorEntity entity);

    List<InstructorResponse> toResponseList(List<InstructorEntity> entities);
}
