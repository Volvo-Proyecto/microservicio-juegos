package com.volvo.volvo.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="juegos")
@Schema(description = "el modelo de un Juego")
@AllArgsConstructor
@NoArgsConstructor
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description ="Id de un Juego",example = "1",requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    @Schema(description ="descripcion de un Juego",example = "Juego sobre dragones bla bla",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String descripcion;

    @Column(nullable = false)
    private Integer anioLanzamiento;

    @Column(nullable = false)
    private Long generoId;

    @Column(nullable = false)
    private Long plataformaId;

    @Column(nullable = false)
    private Long estudioId;
}
