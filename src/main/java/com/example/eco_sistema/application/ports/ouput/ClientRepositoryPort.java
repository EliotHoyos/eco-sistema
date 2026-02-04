package com.example.eco_sistema.application.ports.ouput;

import com.example.eco_sistema.application.ports.generic.GenericCreate;
import com.example.eco_sistema.application.ports.generic.GenericCreateEntity;
import com.example.eco_sistema.application.ports.generic.GenericDelete;
import com.example.eco_sistema.application.ports.generic.GenericList;
import com.example.eco_sistema.application.ports.generic.GenericPaginate;
import com.example.eco_sistema.application.ports.generic.GenericRead;
import com.example.eco_sistema.application.ports.generic.GenericUpdate;
import com.example.eco_sistema.domain.models.request.ClientRequest;
import com.example.eco_sistema.domain.models.response.ClientResponse;

public interface ClientRepositoryPort
        extends GenericCreate<ClientRequest>,
                GenericCreateEntity<ClientRequest>,
                GenericRead<ClientResponse>,
                GenericUpdate<ClientRequest>,
                GenericDelete,
                GenericPaginate<ClientResponse>,
                GenericList<ClientResponse> {}
