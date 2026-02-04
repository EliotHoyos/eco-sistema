package com.example.eco_sistema.infrastructure.controller;

import com.example.eco_sistema.application.service.ClientService;
import com.example.eco_sistema.domain.models.dto.SuccessResponse;
import com.example.eco_sistema.domain.models.enums.DocumentType;
import com.example.eco_sistema.domain.models.request.ClientRequest;
import com.example.eco_sistema.domain.models.response.ClientResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@Tag(name = "Clientes", description = "Endpoints para la gestión de clientes")
public class ClientController {

    private final ClientService clientService;

    // ─── GET ────────────────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Listar todos los clientes",
            description = "Retorna la lista completa de clientes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        return ResponseEntity.ok(clientService.getList());
    }

    // ─── POST ───────────────────────────────────────────────────────────────────

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crear un nuevo cliente",
            description = "Crea un cliente con foto obligatoria enviada como multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o imagen incorrecta"),
            @ApiResponse(responseCode = "409", description = "Ya existe un cliente con ese email o documento")
    })
    public ResponseEntity<SuccessResponse> createClient(
            @Parameter(description = "Foto del cliente (jpg, jpeg, png, máx 5MB)", required = true)
            @RequestParam("photo") MultipartFile file,
            @Parameter(description = "Nombre", required = true)
            @RequestParam("name") String name,
            @Parameter(description = "Apellido", required = true)
            @RequestParam("last_name") String lastName,
            @Parameter(description = "Tipo de documento (DNI o RUC)", required = true)
            @RequestParam("document_type") DocumentType documentType,
            @Parameter(description = "Número de documento", required = true)
            @RequestParam("document") String document,
            @Parameter(description = "Dirección", required = true)
            @RequestParam("address") String address,
            @Parameter(description = "Celular (9 dígitos, empezar con 9)", required = true)
            @RequestParam("cellphome") String cellphome,
            @Parameter(description = "Email", required = true)
            @RequestParam("email") String email,
            @Parameter(description = "Género (Masculino, Femenino u Otro)", required = true)
            @RequestParam("gender") String gender,
            @Parameter(description = "Fecha de nacimiento (yyyy-MM-dd)", required = true)
            @RequestParam("birthday") String birthday
    ) {
        ClientRequest request = ClientRequest.builder()
                .name(name).last_name(lastName).document_type(documentType)
                .document(document).address(address).cellphome(cellphome)
                .email(email).gender(gender).birthday(LocalDate.parse(birthday))
                .photo("temp")
                .build();

        clientService.createClient(file, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                SuccessResponse.builder().Succes("Cliente creado exitosamente").build());
    }
}
