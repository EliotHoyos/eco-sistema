package com.example.eco_sistema.application.ports.generic;

public interface GenericUpdate<R> {
    void update(R request, Long id);
}
