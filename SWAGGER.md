# Swagger / OpenAPI en Eco Sistema

## Qué es Swagger

Swagger es un conjunto de herramientas que permite **documentar automáticamente** las APIs REST. En este proyecto se usa **SpringDoc OpenAPI**, que integra Swagger con Spring Boot sin configuración manual de cada endpoint.

La documentación se genera en tiempo de compilación a partir de las anotaciones del código.

---

## Dependencias (pom.xml)

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-api</artifactId>
    <version>2.3.0</version>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

| Dependencia | Responsabilidad |
|---|---|
| `starter-webmvc-api` | Genera el JSON de la especificación OpenAPI (`/v3/api-docs`) |
| `starter-webmvc-ui` | Sirve la interfaz gráfica Swagger UI (`/swagger-ui/index.html`) |

---

## Configuración global (SwaggerConfig.java)

```java
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Eco Sistema API")
                        .version("1.0.0"));
    }
}
```

Este bean define el **título y versión** que aparecen en la portada de Swagger UI. No se necesita nada más para que SpringDoc descombre los controllers automáticamente.

---

## Anotaciones usadas en los Controllers

### @Tag — Agrupa endpoints

```java
@Tag(name = "Instructores", description = "Endpoints para la gestión de instructores")
public class InstructorController { ... }
```

Crea una sección en Swagger UI que agrupa todos los endpoints de ese controller.

### @Operation — Describe un endpoint

```java
@Operation(
    summary = "Crear un nuevo instructor",
    description = "Crea un instructor con foto obligatoria enviada como multipart/form-data"
)
```

| Campo | Qué muestra |
|---|---|
| `summary` | Título corto del endpoint en la lista |
| `description` | Texto largo al expandir el endpoint |

### @ApiResponses / @ApiResponse — Documenta las respuestas HTTP

```java
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Instructor creado exitosamente"),
    @ApiResponse(responseCode = "400", description = "Datos inválidos o imagen incorrecta"),
    @ApiResponse(responseCode = "409", description = "Ya existe un instructor con ese email")
})
```

Cada `@ApiResponse` aparece como una pestaña de respuesta posible dentro del endpoint.

### @Parameter — Documenta un parámetro individual

```java
@Parameter(description = "Foto del instructor (jpg, jpeg, png, máx 5MB)", required = true)
@RequestParam("photo") MultipartFile file
```

Agrega descripción y marca si es obligatorio directamente sobre cada parámetro del método.

---

## URLs de acceso

| URL | Contenido |
|---|---|
| `http://localhost:8080/swagger-ui/index.html` | Interfaz gráfica interactiva |
| `http://localhost:8080/v3/api-docs` | JSON de la especificación OpenAPI |

---

## Endpoints documentados

### Instructores (`/api/instructors`)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/instructors` | Listar todos los instructores |
| POST | `/api/instructors` | Crear un nuevo instructor (foto obligatoria) |
| PUT | `/api/instructors/{id}` | Editar un instructor existente (foto opcional) |
| PATCH | `/api/instructors/{id}/publish` | Publicar un instructor |
| PATCH | `/api/instructors/{id}/unpublish` | Despublicar un instructor |
| PATCH | `/api/instructors/{id}/inactivar` | Dar de baja (status → inactive) |

### Clientes (`/api/clients`)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/clients` | Listar todos los clientes |
| POST | `/api/clients` | Crear un nuevo cliente (foto obligatoria) |
