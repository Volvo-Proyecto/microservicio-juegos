package com.volvo.volvo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvo.volvo.Assembler.JuegoClassAssembler;
import com.volvo.volvo.Controller.JuegoController;
import com.volvo.volvo.DTO.JuegoPedidoDTO;
import com.volvo.volvo.DTO.JuegoRespuestaDTO;
import com.volvo.volvo.JuegosService.JuegoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(JuegoController.class)
class JuegoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JuegoService juegoService;

    @MockBean
    private JuegoClassAssembler assembler; 

    @Autowired
    private ObjectMapper objectMapper;

    private JuegoRespuestaDTO juegoRespuesta;
    private JuegoPedidoDTO juegoPedido;

    @BeforeEach
    void setUp() {
        juegoRespuesta = new JuegoRespuestaDTO();
        juegoRespuesta.setId(1L);
        juegoRespuesta.setTitulo("Dark Souls");
        juegoRespuesta.setDescripcion("Juego dificil"); 
        
        juegoPedido = new JuegoPedidoDTO();
        juegoPedido.setTitulo("Dark Souls");
        juegoPedido.setDescripcion("Juego dificil");
        juegoPedido.setGeneroId(1L);
        juegoPedido.setPlataformaId(1L);
        juegoPedido.setEstudioId(1L);
    }

  

    @Test
    void deberiaObtenerJuegoPorId() throws Exception {
        when(juegoService.obtenerJuegoporId(1L)).thenReturn(juegoRespuesta);
    
        when(assembler.toModel(any(JuegoRespuestaDTO.class))).thenReturn(EntityModel.of(juegoRespuesta));

        mockMvc.perform(get("/api/v1/juegos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Dark Souls"));
    }

    @Test
    void deberiaCrearJuegoCorrectamente() throws Exception {
        when(juegoService.crearJuego(any(JuegoPedidoDTO.class))).thenReturn(juegoRespuesta);

        mockMvc.perform(post("/api/v1/juegos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(juegoPedido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Dark Souls"));
    }

    @Test
    void deberiaActualizarJuegoExistente() throws Exception {
        when(juegoService.actualizar(eq(1L), any(JuegoPedidoDTO.class))).thenReturn(juegoRespuesta);

        mockMvc.perform(put("/api/v1/juegos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(juegoPedido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Dark Souls"));
    }

    @Test
    void deberiaEliminarJuegoExistente() throws Exception {
        doNothing().when(juegoService).borrarjuego(1L);

        mockMvc.perform(delete("/api/v1/juegos/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}