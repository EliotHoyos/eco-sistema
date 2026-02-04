# 🌿 Eco Sistema

<div align="center">

![Java](https://img.shields.io/badge/Java-17-F58220?style=flat-for-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-6CB33F?style=flat-for-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-F29513?style=flat-for-badge&logo=mysql&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-1.5.5-1B978D?style=flat-for-badge)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-85EA01?style=flat-for-badge&logo=swagger&logoColor=white)


Sistema multipropósito construido con **Arquitectura Hexagonal** y principios de **Clean Architecture** sobre Spring Boot.

</div>

---

## 📋 Tabla de Contenido

- [Descripción del Proyecto](#-descripción-del-proyecto)
- [Arquitectura Hexagonal](#-arquitectura-hexagonal)
- [Patrones de Diseño](#-patrones-de-diseño)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Flujo de Datos](#-flujo-de-datos)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Requisitos](#-requisitos)
- [Configuración y Ejecución](#-configuración-y-ejecución)
- [Ejecución con Docker](#-ejecución-con-docker)
- [Variables de Entorno](#-variables-de-entorno)
- [Documentación de la API](#-documentación-de-la-api)
- [Tecnologías](#-tecnologías)
- [Buenas Prácticas y SOLID](#-buenas-prácticas-y-solid)

---

## 📌 Descripción del Proyecto

**Eco Sistema** es una aplicación REST desarrollada en Java 17 con Spring Boot 3.2.1. Implementa la gestión de **Clientes** e **Instructores**, incluyendo validación de datos, subida de imágenes y auditoría automática de registros.

El sistema está diseñado desde cero siguiendo la **Arquitectura Hexagonal (Ports & Adapters)**, garantizando un núcleo de dominio independiente de la infraestructura y facilitando la testabilidad y escalabilidad del proyecto.

---

## 🏗️ Arquitectura Hexagonal

La Arquitectura Hexagonal, planteada por Alistair Cockburn, separa la lógica de negocio de los mecanismos de entrada y salida mediante **puertos** (interfaces) y **adaptadores** (implementaciones).

```
┌─────────────────────────────────────────────────────────────────┐
│                      INFRAESTRUCTURA                             │
│                                                                  │
│   ┌──────────────┐                         ┌──────────────────┐ │
│   │ Controllers  │                         │  Repository      │ │
│   │ (REST API)   │                         │  Adapters        │ │
│   └──────┬───────┘                         └────────┬─────────┘ │
│          │                                           │           │
│   ┌──────▼───────┐                         ┌────────▼─────────┐ │
│   │   INPUT      │                         │    OUTPUT         │ │
│   │   PORTS      │◄─────────────────────►  │    PORTS          │ │
│   │ (Use Cases)  │                         │ (Repositories)    │ │
│   └──────┬───────┘                         └────────┬─────────┘ │
│          │              ┌──────────────┐             │           │
│          │              │   DOMINIO    │             │           │
│          └─────────────►│  (Business  │◄────────────┘           │
│                         │   Logic)    │                          │
│                         └──────┬───────┘                         │
│                                │                                 │
│                     ┌──────────▼──────────┐                     │
│                     │  APPLICATION LAYER  │                     │
│                     │   (Services)        │                     │
│                     └─────────────────────┘                     │
└─────────────────────────────────────────────────────────────────┘
```

### Capas del Sistema

| Capa | Paquete | Responsabilidad |
|------|---------|-----------------|
| **Domain** | `domain/` | Modelos, enums, excepciones personalizadas y reglas de validación del negocio |
| **Application** | `application/` | Puertos genéricos, casos de uso y servicios de orquestración |
| **Infrastructure** | `infrastructure/` | Controllers REST, repositorios JPA, adaptadores, configuración y AOP |

---

## 🎯 Patrones de Diseño

### 1. DTO — Data Transfer Object
Separa los datos que circulan entre capas de las entidades de persistencia. Cada modelo tiene su representación específica según su dirección en el flujo:

| Clase | Dirección | Propósito |
|-------|:---------:|-----------|
| `ClientRequest` | Entrada | Recibe y valida datos del cliente desde el controller |
| `ClientResponse` | Salida | Devuelve datos del cliente al consumidor |
| `InstructorRequest` | Entrada | Recibe y valida datos del instructor |
| `InstructorResponse` | Salida | Devuelve datos del instructor |
| `ClientDto` | Interna | Representación liviana para uso interno |
| `ErrorDto` | Salida | Formato estandarizado de errores |
| `SuccessResponse` | Salida | Formato estandarizado de respuestas exitosas |

### 2. Repository
Abstrae el acceso a datos separando la lógica de persistencia de la lógica de negocio. Se implementa en dos niveles:

```
RepositoryPort (contrato)  ──►  RepositoryAdapter (lógica)  ──►  JpaRepository (Spring Data)
```

- `ClientRepositoryPort` / `InstructorRepositoryPort` → Definen el contrato que debe cumplir la persistencia
- `ClientRepositoryAdapter` / `InstructorRepositoryAdapter` → Implementan ese contrato, orquestrando mapper + JPA
- `ClientJpaRepository` / `InstructorJpaRepository` → Repositorios de Spring Data que hablan con MySQL

### 3. Adapter
Los adaptadores de repositorio (`ClientRepositoryAdapter`, `InstructorRepositoryAdapter`) conectan la interfaz que el dominio espera con la implementación concreta de Spring Data JPA. Traducen entre objetos de dominio y entidades JPA usando los mappers.

### 4. Builder
Todos los DTOs del proyecto soportan construcción fluida mediante Lombok:
- `@Builder` en `ClientDto`, `ErrorDto`, `SuccessResponse`
- `@SuperBuilder` en `ClientRequest`, `ClientResponse`, `InstructorRequest`, `InstructorResponse`

Se usa activamente en los controllers para construir las peticiones desde los parámetros multipart.

### 5. Strategy (Validación por tipo de documento)
`DocumentType` define las estrategias de validación. Cada enum porta sus propias reglas, y `ClientRequest.isDocumentNumberValid()` selecciona la estrategia según el tipo:

```
DocumentType.DNI  →  regex de 8 dígitos
DocumentType.RUC  →  regex de 11 dígitos
```

La lógica de selección vive en el dominio, sin que el resto del sistema conozca las reglas internas.

### 6. Facade (GlobalExceptionHandler)
`GlobalExceptionHandler` expone una única cara al sistema de manejo de errores. Los controllers no capturan excepciones: las lanzan, y el handler centralizado las traduce a respuestas HTTP estandarizadas (`ErrorDto`) con el código de estado apropiado.

### 7. Singleton (contenedor de Spring)
Todas las clases anotadas con `@Service`, `@Component`, `@Repository` y `@Aspect` son instancias únicas gestionadas por el contenedor de Spring:
- `ClientService`, `InstructorService`
- `FileStorageServiceImpl`, `ImageValidator`
- `ClientRepositoryAdapter`, `InstructorRepositoryAdapter`
- `LoggingAspect`, `GlobalExceptionHandler`

---

## 📁 Estructura del Proyecto

```
eco-sistema/
├── src/
│   ├── main/
│   │   ├── java/com/example/eco_sistema/
│   │   │   ├── domain/                         # Capa de Dominio
│   │   │   │   ├── exception/                  #   Excepciones personalizadas
│   │   │   │   │   ├── DuplicateResourceException.java
│   │   │   │   │   ├── FileStorageException.java
│   │   │   │   │   ├── InvalidDataException.java
│   │   │   │   │   └── ResourceNotFoundException.java
│   │   │   │   ├── models/                     #   Modelos del dominio
│   │   │   │   │   ├── dto/                    #     DTOs intermedios
│   │   │   │   │   ├── enums/                  #     Enumeraciones (DocumentType, InstructorStatus)
│   │   │   │   │   ├── request/                #     Objetos de entrada (ClientRequest, InstructorRequest)
│   │   │   │   │   └── response/               #     Objetos de salida (ClientResponse, InstructorResponse)
│   │   │   │   └── utils/                      #   Utilidades del dominio
│   │   │   │       ├── ImageValidator.java
│   │   │   │       └── ValidationUtils.java
│   │   │   │
│   │   │   ├── application/                    # Capa de Aplicación
│   │   │   │   ├── ports/
│   │   │   │   │   ├── generic/                #   Puertos genéricos (CRUD base)
│   │   │   │   │   ├── input/                  #   Puertos de entrada (Use Cases)
│   │   │   │   │   │   ├── ClientUseCase.java
│   │   │   │   │   │   └── InstructorUseCase.java
│   │   │   │   │   └── output/                 #   Puertos de salida (Repository Ports)
│   │   │   │   │       ├── ClientRepositoryPort.java
│   │   │   │   │       └── InstructorRepositoryPort.java
│   │   │   │   └── service/                    #   Servicios de orquestración
│   │   │   │       ├── ClientService.java
│   │   │   │       ├── FileStorageService.java
│   │   │   │       ├── FileStorageServiceImpl.java
│   │   │   │       └── InstructorService.java
│   │   │   │
│   │   │   └── infrastructure/                 # Capa de Infraestructura
│   │   │       ├── advice/                     #   Manejo global de excepciones
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       ├── aop/                        #   Aspectos (Logging)
│   │   │       │   └── LoggingAspect.java
│   │   │       ├── config/                     #   Configuración
│   │   │       │   ├── FileUploadConfig.java
│   │   │       │   ├── SecurityConfig.java
│   │   │       │   └── SwaggerConfig.java
│   │   │       ├── controller/                 #   Controladores REST
│   │   │       │   ├── ClientController.java
│   │   │       │   └── InstructorController.java
│   │   │       └── repository/                 #   Capa de persistencia
│   │   │           ├── adapter/                #     Adaptadores de repositorio
│   │   │           │   ├── ClientRepositoryAdapter.java
│   │   │           │   └── InstructorRepositoryAdapter.java
│   │   │           ├── entities/               #     Entidades JPA
│   │   │           │   ├── Auditable.java
│   │   │           │   ├── ClientEntity.java
│   │   │           │   └── InstructorEntity.java
│   │   │           ├── jpa/                    #     Repositorios Spring Data
│   │   │           │   ├── ClientJpaRepository.java
│   │   │           │   └── InstructorJpaRepository.java
│   │   │           └── mapper/                 #     Mappers (MapStruct)
│   │   │               ├── ClientMapper.java
│   │   │               └── InstructorMapper.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/com/example/eco_sistema/
│           └── domain/utils/
│               └── ImageValidatorTest.java
│
├── uploads/                                    # Almacenamiento de imágenes
├── docker-compose.yml
└── pom.xml
```

---

## 🔄 Flujo de Datos

El siguiente diagrama representa el flujo completo desde una petición HTTP hasta la persistencia en la base de datos (ejemplo: creación de un Instructor):

```
  HTTP POST /api/instructors
        │
        ▼
  ┌─────────────────┐
  │  Instructor      │  ← Recibe multipart/form-data
  │  Controller      │
  └────────┬────────┘
           │ Construye InstructorRequest
           ▼
  ┌─────────────────┐
  │  InstructorService│  ← Orquesta el flujo
  └────────┬────────┘
           │
     ┌─────┴──────────────┐
     ▼                    ▼
  ┌──────────┐     ┌────────────────┐
  │ Image    │     │ ValidationUtils │  ← Sanitiza strings
  │ Validator│     └────────────────┘
  └──────────┘
     │
     ▼ (si es válida)
  ┌─────────────────┐
  │ FileStorage     │  ← Guarda imagen con nombre UUID
  │ Service         │     en /uploads/instructors/
  └────────┬────────┘
           ▼
  ┌─────────────────┐
  │ Jakarta Bean    │  ← Valida @NotBlank, @Pattern, @Size
  │ Validation      │
  └────────┬────────┘
           ▼
  ┌─────────────────┐
  │ InstructorRepo  │  ← Puerto de salida (contrato)
  │ Port            │
  └────────┬────────┘
           ▼
  ┌─────────────────┐
  │ InstructorRepo  │  ← Adaptador (implementación)
  │ Adapter         │
  └────────┬────────┘
           │ InstructorMapper.toEntity()
           ▼
  ┌─────────────────┐
  │ InstructorJpa   │  ← Spring Data JPA
  │ Repository      │
  └────────┬────────┘
           ▼
       ┌───────┐
       │  MySQL │
       └───────┘
```

---

## 📡 Endpoints de la API

### Clientes `/api/clients`

| Método | Endpoint | Descripción | Status |
|:------:|----------|-------------|:------:|
| `GET` | `/api/clients` | Obtener todos los clientes | `200` |
| `POST` | `/api/clients` | Crear un nuevo cliente (con foto) | `201` |

### Instructores `/api/instructors`

| Método | Endpoint | Descripción | Status |
|:------:|----------|-------------|:------:|
| `GET` | `/api/instructors` | Obtener todos los instructores | `200` |
| `POST` | `/api/instructors` | Crear un nuevo instructor (con foto) | `201` |
| `PUT` | `/api/instructors/{id}` | Actualizar instructor (foto opcional) | `200` |
| `PATCH` | `/api/instructors/{id}/publish` | Publicar instructor | `200` |
| `PATCH` | `/api/instructors/{id}/unpublish` | Despublicar instructor | `200` |
| `PATCH` | `/api/instructors/{id}/inactivar` | Inactivar instructor | `200` |

### Excepciones Controladas

| Excepción | Código HTTP | Ejemplo |
|-----------|:-----------:|---------|
| `ResourceNotFoundException` | `404` | Instructor con ID no encontrado |
| `DuplicateResourceException` | `409` | Email o documento ya registrado |
| `InvalidDataException` | `400` | Campos no cumplen reglas de validación |
| `FileStorageException` | `400` | Formato de imagen no soportado |

---

## ⚙️ Requisitos

| Herramienta | Versión mínima |
|-------------|:--------------:|
| JDK | 17 |
| Maven | 3.6+ |
| MySQL | 8.0 |
| Docker (opcional) | 24+ |

---

## 🚀 Configuración y Ejecución

### 1. Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd eco-sistema
```

### 2. Configurar la base de datos

Crear la base de datos en MySQL:

```sql
CREATE DATABASE db-ecosistema;
```

### 3. Configurar `application.properties`

Actualizar las credenciales de conexión en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db-ecosistema
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

### 4. Compilar y ejecutar

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

---

## 🐳 Ejecución con Docker

El archivo `docker-compose.yml` levanta el entorno completo:

| Servicio | Puerto | Descripción |
|----------|:------:|-------------|
| MySQL | `3307` | Base de datos |
| eco-sistema | `8080` | Aplicación Spring Boot |
| phpMyAdmin | `8081` | Administrador de BD visual |

```bash
docker compose up -d
```

---

## 🔧 Variables de Entorno

| Variable | Valor por defecto | Descripción |
|----------|:-----------------:|-------------|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/db-ecosistema` | URL de conexión MySQL |
| `spring.datasource.pool-size` | `10` | Máximo de conexiones (HikariCP) |
| `spring.jpa.hibernate.ddl-auto` | `update` | Estrategia de esquema |
| `spring.servlet.multipart.max-file-size` | `5MB` | Tamaño máximo por archivo |
| `spring.servlet.multipart.max-request-size` | `10MB` | Tamaño máximo de petición |
| `app.upload-dir` | `uploads` | Directorio local de imágenes |

---

## 📖 Documentación de la API

Una vez que la aplicación esté en ejecución:

| Recurso | URL |
|---------|-----|
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| Imágenes subidas | `http://localhost:8080/uploads/{carpeta}/{archivo}` |

---

## 🛠️ Tecnologías

<div align="center">

| Categoría | Tecnología |
|-----------|:----------:|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.2.1 |
| Persistencia | Spring Data JPA / Hibernate |
| Base de datos | MySQL 8.0 |
| Mapeo | MapStruct 1.5.5 |
| Validación | Jakarta Bean Validation |
| Seguridad | Spring Security |
| Documentación | SpringDoc OpenAPI / Swagger UI |
| AOP | Spring AOP |
| Boilerplate | Lombok |
| Build | Maven |
| Contenedores | Docker / Docker Compose |

</div>

---

## 📐 Buenas Prácticas y SOLID

### SOLID

#### S — Single Responsibility Principle (SRP)
Cada clase tiene una única razón para cambiar:

| Clase | Responsabilidad única |
|-------|-----------------------|
| `ClientService` | Orquestración de la lógica de creación de clientes |
| `InstructorService` | Orquestración de la lógica de instructores |
| `FileStorageServiceImpl` | Almacenamiento y eliminación de archivos en disco |
| `ImageValidator` | Validación de formato y tamaño de imágenes |
| `ValidationUtils` | Sanitización de strings |
| `LoggingAspect` | Logging transversal |
| `GlobalExceptionHandler` | Traducción de excepciones a respuestas HTTP |
| `ClientMapper` / `InstructorMapper` | Conversión de tipos entre capas |

#### O — Open/Closed Principle (OCP)
El sistema se extiende sin modificar código existente:
- Los **Generic Ports** (`GenericCreate`, `GenericRead`, etc.) son contratos abiertos a nuevos casos de uso. Al agregar un nuevo recurso se implementan sin tocar las interfaces existentes.
- `GlobalExceptionHandler` se extiende agregando nuevos métodos `@ExceptionHandler` sin alterar los ya definidos.
- Los **mappers** de MapStruct se generan automáticamente; agregar un campo al modelo no requiere cambiar lógica manual.

#### L — Liskov Substitution Principle (LSP)
Las implementaciones cumplen íntegramente el contrato de sus interfaces:
- `ClientRepositoryAdapter` sustituye a `ClientRepositoryPort` sin romper el comportamiento esperado por el servicio.
- `InstructorRepositoryAdapter` sustituye a `InstructorRepositoryPort` ídem.
- `FileStorageServiceImpl` sustituye a `FileStorageService` sin que el servicio que la inyecta conozca la implementación.

#### I — Interface Segregation Principle (ISP)
Las interfaces son pequeñas y enfocadas. Los puertos genéricos dividen las responsabilidades en una interfaz por operación:

```
GenericCreate<R>      →  save()
GenericRead<T>        →  getDomain()
GenericUpdate<R>      →  update()
GenericDelete         →  delete()
GenericList<T>        →  getList()
GenericPaginate<T>    →  getPagination()
```

`ClientUseCase` e `InstructorUseCase` componen solo las que necesitan, en lugar de depender de una interfaz monolítica.

#### D — Dependency Inversion Principle (DIP)
Los módulos de alto nivel dependen de abstracciones, no de implementaciones concretas:
- `ClientService` recibe `ClientRepositoryPort` (interfaz), nunca `ClientRepositoryAdapter`.
- `InstructorService` recibe `InstructorRepositoryPort` (interfaz), nunca el adaptador concreto.
- Ambos servicios reciben `FileStorageService` (interfaz), nunca `FileStorageServiceImpl`.
- La inyección se resuelve por constructor con `@RequiredArgsConstructor`, sin instanciación manual.

---

### Otras Buenas Prácticas

#### DRY — Don't Repeat Yourself
- `Auditable` (clase base con `@MappedSuperclass`) centraliza `created_at` y `updated_at` para todas las entidades. Sin ella, cada entidad repetiría los mismos campos y hooks.
- `ValidationUtils.sanitizeString()` se reutiliza en `ClientService` e `InstructorService` para normalizar strings.
- `ImageValidator` se inyecta en ambos servicios, evitando duplicar la lógica de validación de archivos.
- Los Generic Ports evitan redefinir los métodos CRUD en cada puerto específico.

#### Separation of Concerns (SoC)
Cada capa tiene responsabilidades claramente delimitadas y no se mezclan:

| Preocupación | Donde vive |
|--------------|------------|
| Reglas del negocio | `domain/` |
| Orquestración de flujos | `application/service/` |
| Contrato de entrada/salida | `application/ports/` |
| Comunicación HTTP | `infrastructure/controller/` |
| Persistencia | `infrastructure/repository/` |
| Logging transversal | `infrastructure/aop/` |
| Manejo de errores | `infrastructure/advice/` |

#### AOP — Aspect-Oriented Programming
`LoggingAspect` implementa logging de método (entrada, salida, tiempo de ejecución, excepciones) como una preocupación transversal. Los servicios y controllers no contienen una sola línea de logging operativo.

#### Auditoría automática de entidades
`Auditable` con `@EntityListeners(AuditingEntityListener.class)` y `@EnableJpaAuditing` gestiona automáticamente los timestamps de creación y actualización. Las entidades no implementan esa lógica.

#### Validación en capas
La validación no vive en un solo lugar; se distribuye según su naturaleza:
- **Capa de entrada:** Jakarta Bean Validation con anotaciones (`@NotBlank`, `@Pattern`, `@Size`, `@Email`)
- **Capa de dominio:** Reglas de negocio custom en `isDocumentNumberValid()` dentro del DTO de request
- **Capa de servicio:** Validación de archivos (`ImageValidator`) y sanitización (`ValidationUtils`)
- **Capa de persistencia:** Restricciones a nivel de base de datos (`unique`) capturadas por `GlobalExceptionHandler`

