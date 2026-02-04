package com.example.eco_sistema.domain.models.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ErrorDto {
    private List<String> errores;
}
