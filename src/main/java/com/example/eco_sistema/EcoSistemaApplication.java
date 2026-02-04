package com.example.eco_sistema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EcoSistemaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcoSistemaApplication.class, args);
	}

}
