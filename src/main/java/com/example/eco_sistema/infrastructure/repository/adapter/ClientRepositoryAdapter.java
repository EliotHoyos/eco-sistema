package com.example.eco_sistema.infrastructure.repository.adapter;

import com.example.eco_sistema.application.ports.ouput.ClientRepositoryPort;
import com.example.eco_sistema.domain.models.request.ClientRequest;
import com.example.eco_sistema.domain.models.response.ClientResponse;
import com.example.eco_sistema.infrastructure.repository.mapper.ClientMapper;
import com.example.eco_sistema.infrastructure.repository.entities.ClientEntity;
import com.example.eco_sistema.infrastructure.repository.jpa.ClientJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientRepositoryAdapter implements ClientRepositoryPort {

    private final ClientJpaRepository clientJpaRepository;
    private final ClientMapper clientMapper;

    @Override
    public void save(ClientRequest request) {
        ClientEntity entity = clientMapper.toEntity(request);
        clientJpaRepository.save(entity);
    }

    @Override
    public Object saveEntity(ClientRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<ClientResponse> getList() {
        List<ClientEntity> entities = clientJpaRepository.findAll();
        return clientMapper.toResponseList(entities);
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
}
