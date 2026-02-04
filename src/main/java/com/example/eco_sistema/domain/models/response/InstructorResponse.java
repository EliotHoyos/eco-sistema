package com.example.eco_sistema.domain.models.response;

import com.example.eco_sistema.domain.models.enums.InstructorStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InstructorResponse {
    private Long id;
    private String name;
    private String last_name;
    private String specialty;
    private String belt_level;
    private String bio;
    private String photo;
    private String email;
    private String phone;
    private Integer experience_years;
    private String certifications;
    private String social_media_facebook;
    private String social_media_instagram;
    private String social_media_twitter;
    private InstructorStatus status;
    private Boolean is_published;
}
