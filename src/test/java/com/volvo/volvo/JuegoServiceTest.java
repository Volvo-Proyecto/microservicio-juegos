package com.volvo.volvo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.volvo.volvo.DTO.JuegoRespuestaDTO;
import com.volvo.volvo.JuegosRepository.JuegoRepository;
import com.volvo.volvo.JuegosService.JuegoService;

import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class JuegoServiceTest {

    @Mock
    private JuegoRepository repositorio;

    @Mock
    private WebClient generowebClient;

    @Mock
    private WebClient plataformawebClient;

    @Mock
    private WebClient estudiowebClient;

    @Spy
    @InjectMocks
    private JuegoService servicio;


    @Test
    void listarjuegos_deberiaRetornarListaJuegos(){
        com.volvo.volvo.Model.Juego juego = new com.volvo.volvo.Model.Juego();
        juego.setId(7L);
        juego.setTitulo("Furrylove");
        juego.setDescripcion("juego para trolls");
        juego.setAnioLanzamiento(2010);
        juego.setGeneroId(1L);
        juego.setPlataformaId(1L);
        juego.setEstudioId(1L);

        when(repositorio.findAll()).thenReturn(List.of(juego));

        JuegoRespuestaDTO dto = new JuegoRespuestaDTO();
        dto.setDescipcion("juego para trolls");
        dto.setId(7L);
        dto.setAnioLanzamiento(2010);
        dto.setTitulo("Furrylove");

        doReturn(dto).when(servicio).enriquecerJuego(any(com.volvo.volvo.Model.Juego.class));

        List<JuegoRespuestaDTO> lista = servicio.obtenerJuegos();

        assertEquals(1, lista.size());
        assertEquals("juego para trolls", lista.get(0).getDescipcion());

        verify(repositorio,times(1)).findAll();
    }

    


}
