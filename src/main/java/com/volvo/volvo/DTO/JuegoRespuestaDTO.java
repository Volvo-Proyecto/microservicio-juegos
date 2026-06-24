package com.volvo.volvo.DTO;

import lombok.Data;

@Data
public class JuegoRespuestaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Integer anioLanzamiento;


    private GeneroDTO genero;
    private PlataformaDTO plataforma;
    private EstudioDTO estudio;

}
