package com.volvo.volvo;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean; // <-- CAMBIÓ ESTA LÍNEA
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvo.volvo.Controller.JuegoController;
import com.volvo.volvo.DTO.JuegoRespuestaDTO;
import com.volvo.volvo.JuegosService.JuegoService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/************** PRUEBAS UNITARIAS EN EL CONTROLLER ***********************/
@WebMvcTest(JuegoController.class)
public class JuegoControllerTest {

    @MockBean // Se cambia la anotación de MockitoBean a MockBean para que Spring Boot pueda inyectar el mock en el contexto de la aplicación.
    private JuegoService servicio;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    @Test
    void listarTodos_deberiaRetornar200() throws Exception { 
        JuegoRespuestaDTO j = new JuegoRespuestaDTO();
        j.setDescripcion("juego para trolls");
        j.setId(7L);
        j.setAnioLanzamiento(2010);
        j.setTitulo("Furrylove");

        when(servicio.obtenerJuegos()).thenReturn(List.of(j));

        mockMvc.perform(get("/api/v1/juegos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(7L))
            .andExpect(jsonPath("$[0].titulo").value("Furrylove"));
    } 
}