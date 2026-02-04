package com.example.eco_sistema.infrastructure.repository.entities;

import com.example.eco_sistema.domain.models.enums.InstructorStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "instructors")
public class InstructorEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String last_name;
    private String specialty;
    private String belt_level;
    @Column(columnDefinition = "TEXT")
    private String bio;
    private String photo;
    @Column(unique = true)
    private String email;
    private String phone;
    private Integer experience_years;
    @Column(columnDefinition = "TEXT")
    private String certifications;
    private String social_media_facebook;
    private String social_media_instagram;
    private String social_media_twitter;
    @Enumerated(EnumType.STRING)
    private InstructorStatus status;
    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = false;
}
