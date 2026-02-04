package com.example.eco_sistema.infrastructure.repository.jpa;

import com.example.eco_sistema.infrastructure.repository.entities.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorJpaRepository extends JpaRepository<InstructorEntity, Long> {
}
