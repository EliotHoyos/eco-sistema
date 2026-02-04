package com.example.eco_sistema.domain.models.request;

import com.example.eco_sistema.domain.models.enums.InstructorStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
public class InstructorRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "El nombre no puede ser null")
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;

    @NotNull(message = "El apellido no puede ser null")
    @NotBlank(message = "El apellido no puede estar en blanco")
    private String last_name;

    @NotNull(message = "La especialidad no puede ser null")
    @NotBlank(message = "La especialidad no puede estar en blanco")
    @Size(max = 255, message = "La especialidad no puede exceder 255 caracteres")
    private String specialty;

    @NotNull(message = "El nivel de cinturón no puede ser null")
    @NotBlank(message = "El nivel de cinturón no puede estar en blanco")
    @Size(max = 50, message = "El nivel de cinturón no puede exceder 50 caracteres")
    private String belt_level;

    @Size(max = 5000, message = "La biografía no puede exceder 5000 caracteres")
    private String bio;

    @NotBlank(message = "La foto no puede estar en blanco")
    private String photo;

    @NotBlank(message = "El correo no puede estar en blanco")
    @Email(message = "El correo debe ser válido")
    private String email;

    @NotBlank(message = "El teléfono no puede estar en blanco")
    @Pattern(
            regexp = "^9\\d{8}$",
            message = "El número de teléfono debe tener 9 dígitos y comenzar con 9")
    private String phone;

    @NotNull(message = "Los años de experiencia no pueden ser null")
    @Min(value = 0, message = "Los años de experiencia deben ser un valor positivo")
    @Max(value = 100, message = "Los años de experiencia no pueden exceder 100")
    private Integer experience_years;

    @Size(max = 5000, message = "Las certificaciones no pueden exceder 5000 caracteres")
    private String certifications;

    @Size(max = 255, message = "El enlace de Facebook no puede exceder 255 caracteres")
    private String social_media_facebook;

    @Size(max = 255, message = "El enlace de Instagram no puede exceder 255 caracteres")
    private String social_media_instagram;

    @Size(max = 255, message = "El enlace de Twitter no puede exceder 255 caracteres")
    private String social_media_twitter;

    private InstructorStatus status;

    private Boolean isPublished;
}
