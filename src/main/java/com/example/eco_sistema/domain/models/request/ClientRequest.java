package com.example.eco_sistema.domain.models.request;

import com.example.eco_sistema.domain.models.enums.DocumentType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
public class ClientRequest implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    @NotNull(message = "El nombre no puede ser null")
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;
    @NotNull(message = "El apellido no puede ser null")
    @NotBlank(message = "El apellido no puede estar en blanco")
    private String last_name;
    @NotNull(message = "El tipo de documento no puede ser null")
    private DocumentType document_type;
    @NotBlank(message = "El número de documento no puede estar en blanco")
    private String document;

    @AssertTrue(message = "El número de documento no es válido para el tipo seleccionado")
    public boolean isDocumentNumberValid(){
        if(document_type == null || document == null){
            return true;
        }
        return switch (document_type){
            case DNI -> document.matches("^\\d{8}$");
            case RUC -> document.matches("^\\d{11}$");
        };
    }
    @NotNull(message = "La dirección no puede ser null")
    @NotBlank(message = "La dirección no puede estar en blanco")
    private String address;
    @NotBlank(message = "El número de celular no puede estar en blanco")
    @Pattern(
            regexp = "^9\\d{8}$",
            message = "El número de celular debe tener 9 dígitos y comenzar con 9")
    private String cellphome;
    @NotBlank(message = "El correo no puede estar en blanco")
    @Email(message = "El correo debe ser válido")
    private String email;
    @NotBlank(message = "El género no puede estar en blanco")
    @Pattern(regexp = "^(Masculino|Femenino|Otro)$", message = "El género debe ser Masculino, Femenino u Otro")
    private String gender;
    @DateTimeFormat(pattern = "DD/MM/YYYY")
    private LocalDate birthday;
    @NotBlank(message = "La foto no puede estar en blanco")
    private String photo;

}
