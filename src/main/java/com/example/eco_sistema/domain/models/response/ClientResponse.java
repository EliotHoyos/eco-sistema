package com.example.eco_sistema.domain.models.response;

import com.example.eco_sistema.domain.models.enums.DocumentType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientResponse {
    private Long id;
    private String name;
    private String last_name;
    private DocumentType document_type;
    private String document;
    private String address;
    private String cellphome;
    private String email;
    private String gender;
    private LocalDate birthday;
    private String photo;
}
