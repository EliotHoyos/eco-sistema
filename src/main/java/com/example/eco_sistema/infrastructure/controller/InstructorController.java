package com.example.eco_sistema.infrastructure.controller;

import com.example.eco_sistema.application.service.InstructorService;
import com.example.eco_sistema.domain.models.dto.SuccessResponse;
import com.example.eco_sistema.domain.models.enums.InstructorStatus;
import com.example.eco_sistema.domain.models.request.InstructorRequest;
import com.example.eco_sistema.domain.models.response.InstructorResponse;
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

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@Tag(name = "Instructores", description = "Endpoints para la gestión de instructores")
public class InstructorController {

    private final InstructorService instructorService;

    // ─── GET ────────────────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Listar todos los instructores",
            description = "Retorna la lista completa de instructores registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<List<InstructorResponse>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getList());
    }

    // ─── POST ───────────────────────────────────────────────────────────────────

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crear un nuevo instructor",
            description = "Crea un instructor con foto obligatoria enviada como multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Instructor creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o imagen incorrecta"),
            @ApiResponse(responseCode = "409", description = "Ya existe un instructor con ese email")
    })
    public ResponseEntity<SuccessResponse> createInstructor(
            @Parameter(description = "Foto del instructor (jpg, jpeg, png, máx 5MB)", required = true)
            @RequestParam("photo") MultipartFile file,
            @Parameter(description = "Nombre", required = true)
            @RequestParam("name") String name,
            @Parameter(description = "Apellido", required = true)
            @RequestParam("last_name") String lastName,
            @Parameter(description = "Especialidad", required = true)
            @RequestParam("specialty") String specialty,
            @Parameter(description = "Nivel de cinturón", required = true)
            @RequestParam("belt_level") String beltLevel,
            @Parameter(description = "Bio")
            @RequestParam(value = "bio", required = false) String bio,
            @Parameter(description = "Email", required = true)
            @RequestParam("email") String email,
            @Parameter(description = "Teléfono (9 dígitos, empezar con 9)", required = true)
            @RequestParam("phone") String phone,
            @Parameter(description = "Años de experiencia", required = true)
            @RequestParam("experience_years") Integer experienceYears,
            @Parameter(description = "Certificaciones")
            @RequestParam(value = "certifications", required = false) String certifications,
            @Parameter(description = "Facebook")
            @RequestParam(value = "social_media_facebook", required = false) String socialMediaFacebook,
            @Parameter(description = "Instagram")
            @RequestParam(value = "social_media_instagram", required = false) String socialMediaInstagram,
            @Parameter(description = "Twitter")
            @RequestParam(value = "social_media_twitter", required = false) String socialMediaTwitter,
            @Parameter(description = "Estado (active/inactive)")
            @RequestParam(value = "status", required = false) InstructorStatus status
    ) {
        InstructorRequest request = InstructorRequest.builder()
                .name(name).last_name(lastName).specialty(specialty).belt_level(beltLevel)
                .bio(bio).email(email).phone(phone).experience_years(experienceYears)
                .certifications(certifications).social_media_facebook(socialMediaFacebook)
                .social_media_instagram(socialMediaInstagram).social_media_twitter(socialMediaTwitter)
                .status(status).photo("temp")
                .build();

        instructorService.createInstructor(file, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                SuccessResponse.builder().Succes("Instructor creado exitosamente").build());
    }

    // ─── PUT ────────────────────────────────────────────────────────────────────

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Editar un instructor existente",
            description = "Actualiza los datos. La foto es opcional: si se envía se reemplaza, si no se mantiene la actual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Instructor no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un instructor con ese email")
    })
    public ResponseEntity<SuccessResponse> updateInstructor(
            @Parameter(description = "ID del instructor", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nueva foto (opcional)")
            @RequestParam(value = "photo", required = false) MultipartFile file,
            @Parameter(description = "Nombre", required = true)
            @RequestParam("name") String name,
            @Parameter(description = "Apellido", required = true)
            @RequestParam("last_name") String lastName,
            @Parameter(description = "Especialidad", required = true)
            @RequestParam("specialty") String specialty,
            @Parameter(description = "Nivel de cinturón", required = true)
            @RequestParam("belt_level") String beltLevel,
            @Parameter(description = "Bio")
            @RequestParam(value = "bio", required = false) String bio,
            @Parameter(description = "Email", required = true)
            @RequestParam("email") String email,
            @Parameter(description = "Teléfono (9 dígitos, empezar con 9)", required = true)
            @RequestParam("phone") String phone,
            @Parameter(description = "Años de experiencia", required = true)
            @RequestParam("experience_years") Integer experienceYears,
            @Parameter(description = "Certificaciones")
            @RequestParam(value = "certifications", required = false) String certifications,
            @Parameter(description = "Facebook")
            @RequestParam(value = "social_media_facebook", required = false) String socialMediaFacebook,
            @Parameter(description = "Instagram")
            @RequestParam(value = "social_media_instagram", required = false) String socialMediaInstagram,
            @Parameter(description = "Twitter")
            @RequestParam(value = "social_media_twitter", required = false) String socialMediaTwitter,
            @Parameter(description = "Estado (active/inactive)")
            @RequestParam(value = "status", required = false) InstructorStatus status
    ) {
        InstructorRequest request = InstructorRequest.builder()
                .name(name).last_name(lastName).specialty(specialty).belt_level(beltLevel)
                .bio(bio).email(email).phone(phone).experience_years(experienceYears)
                .certifications(certifications).social_media_facebook(socialMediaFacebook)
                .social_media_instagram(socialMediaInstagram).social_media_twitter(socialMediaTwitter)
                .status(status).photo("temp")
                .build();

        instructorService.updateInstructor(id, file, request);

        return ResponseEntity.ok(
                SuccessResponse.builder().Succes("Instructor actualizado exitosamente").build());
    }

    // ─── PATCH publicar ─────────────────────────────────────────────────────────

    @PatchMapping("/{id}/publish")
    @Operation(summary = "Publicar un instructor",
            description = "Cambia el estado de publicación a true")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor publicado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Instructor no encontrado")
    })
    public ResponseEntity<InstructorResponse> publishInstructor(
            @Parameter(description = "ID del instructor", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(instructorService.publish(id));
    }

    // ─── PATCH despublicar ──────────────────────────────────────────────────────

    @PatchMapping("/{id}/unpublish")
    @Operation(summary = "Despublicar un instructor",
            description = "Cambia el estado de publicación a false")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor despublicado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Instructor no encontrado")
    })
    public ResponseEntity<InstructorResponse> unpublishInstructor(
            @Parameter(description = "ID del instructor", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(instructorService.unpublish(id));
    }

    // ─── PATCH dar de baja ──────────────────────────────────────────────────────

    @PatchMapping("/{id}/inactivar")
    @Operation(summary = "Dar de baja a un instructor",
            description = "Cambia el status a 'inactive'. No elimina el registro de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor dado de baja exitosamente"),
            @ApiResponse(responseCode = "404", description = "Instructor no encontrado")
    })
    public ResponseEntity<InstructorResponse> inactivateInstructor(
            @Parameter(description = "ID del instructor a dar de baja", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(instructorService.deactivate(id));
    }
}
