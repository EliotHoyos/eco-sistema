package com.example.eco_sistema.domain.models.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientDto {
    private Long id;
    private String name;
    private String lastName;
    private String document;
    private String email;
}
