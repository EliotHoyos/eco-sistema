package com.example.eco_sistema.application.ports.generic;

public interface GenericRead<T> {
    T getDomain(Long id);
}
