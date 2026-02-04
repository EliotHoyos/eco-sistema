package com.example.eco_sistema.infrastructure.repository.entities;

import com.example.eco_sistema.domain.models.enums.DocumentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "clients")
public class ClientEntity extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String last_name;
    @Enumerated(EnumType.STRING)
    private DocumentType document_type;
    @Column(unique = true)
    private String document;
    private String address;
    private String cellphome;
    @Column(unique = true)
    private String email;
    private String gender;
    private LocalDate birthday;
    private String photo;


}
