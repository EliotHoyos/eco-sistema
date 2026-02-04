package com.example.eco_sistema.application.ports.ouput;

import com.example.eco_sistema.application.ports.generic.GenericCreate;
import com.example.eco_sistema.application.ports.generic.GenericCreateEntity;
import com.example.eco_sistema.application.ports.generic.GenericDelete;
import com.example.eco_sistema.application.ports.generic.GenericList;
import com.example.eco_sistema.application.ports.generic.GenericPaginate;
import com.example.eco_sistema.application.ports.generic.GenericRead;
import com.example.eco_sistema.application.ports.generic.GenericUpdate;
import com.example.eco_sistema.domain.models.request.InstructorRequest;
import com.example.eco_sistema.domain.models.response.InstructorResponse;

public interface InstructorRepositoryPort
        extends GenericCreate<InstructorRequest>,
                GenericCreateEntity<InstructorRequest>,
                GenericRead<InstructorResponse>,
                GenericUpdate<InstructorRequest>,
                GenericDelete,
                GenericPaginate<InstructorResponse>,
                GenericList<InstructorResponse> {

    InstructorResponse publish(Long id);

    InstructorResponse unpublish(Long id);

    InstructorResponse deactivate(Long id);
}
