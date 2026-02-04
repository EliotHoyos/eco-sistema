package com.example.eco_sistema.infrastructure.repository.mapper;

import com.example.eco_sistema.domain.models.request.ClientRequest;
import com.example.eco_sistema.domain.models.response.ClientResponse;
import com.example.eco_sistema.infrastructure.repository.entities.ClientEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientEntity toEntity(ClientRequest request);

    ClientResponse toResponse(ClientEntity entity);

    List<ClientResponse> toResponseList(List<ClientEntity> entities);
}
