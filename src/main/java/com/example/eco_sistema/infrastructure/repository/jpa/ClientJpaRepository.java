package com.example.eco_sistema.infrastructure.repository.jpa;

import com.example.eco_sistema.infrastructure.repository.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientJpaRepository extends JpaRepository<ClientEntity, Long> {
}
