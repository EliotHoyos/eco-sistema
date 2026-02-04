package com.example.eco_sistema.application.ports.generic;

public interface GenericCreateEntity<R>{
    Object saveEntity(R request);
}
