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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

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
        dto.setDescripcion("juego para trolls");
        dto.setId(7L);
        dto.setAnioLanzamiento(2010);
        dto.setTitulo("Furrylove");

        doReturn(dto).when(servicio).enriquecerJuego(any(com.volvo.volvo.Model.Juego.class));

        List<JuegoRespuestaDTO> lista = servicio.obtenerJuegos();

        assertEquals(1, lista.size());
        assertEquals("juego para trolls", lista.get(0).getDescripcion());

        verify(repositorio,times(1)).findAll();
    }
    @Test
    void deberiaLanzarExcepcionSiJuegoNoExisteAlObtener() {

        when(repositorio.findById(99L)).thenReturn(Optional.empty());


        RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> servicio.obtenerJuegoporId(99L)
        );
        assertTrue(ex.getMessage().contains("no encontrado"));
    }

    @Test
    void deberiaBorrarJuegoExistente() {
  
        when(repositorio.existsById(1L)).thenReturn(true);

        servicio.borrarjuego(1L);


        verify(repositorio, times(1)).deleteById(1L);
    }

    @Test
    void deberiaLanzarExcepcionAlBorrarJuegoInexistente() {
        when(repositorio.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> servicio.borrarjuego(99L)
        );
        assertTrue(ex.getMessage().contains("no encontrado"));
        verify(repositorio, never()).deleteById(any()); 
    }

    


}
