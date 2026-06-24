package com.volvo.volvo.JuegosService;

import java.util.List;
import java.util.stream.Collectors;



import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.volvo.volvo.DTO.EstudioDTO;
import com.volvo.volvo.DTO.GeneroDTO;
import com.volvo.volvo.DTO.JuegoPedidoDTO;
import com.volvo.volvo.DTO.JuegoRespuestaDTO;
import com.volvo.volvo.DTO.PlataformaDTO;
import com.volvo.volvo.JuegosRepository.JuegoRepository;
import com.volvo.volvo.Model.Juego;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JuegoService {
    
    private final JuegoRepository repositorio;

    private final WebClient generowebClient;
    private final WebClient plataformawebClient;
    private final WebClient estudiowebClient;

//-----metodo para listar todossss-------//
    public List<JuegoRespuestaDTO> obtenerJuegos(){
        return repositorio.findAll()
            .stream()
            .map(this::enriquecerJuego)
            .collect(Collectors.toList());
    }

    public JuegoRespuestaDTO obtenerJuegoporId(Long id){
        Juego juego = repositorio.findById(id)
                    .orElseThrow(()-> new RuntimeException("Juego no encontrado con id;"+id));
        return enriquecerJuego(juego);            
    }

    ///--------CREAR JUego-------//

    public JuegoRespuestaDTO crearJuego(JuegoPedidoDTO pedido){

        //****IMPORTANTE ACTIVAR EL VALIDAR MAS TARDE; AHORA ESTA DESACTIVADO PARA HACER PRUEBAS SIN OTROS SERVICIOS CORRIENDO */
        validarIdExterno(pedido.getGeneroId(), pedido.getPlataformaId(), pedido.getEstudioId());

        Juego juego = new Juego();
        juego.setTitulo(pedido.getTitulo());
        juego.setDescripcion(pedido.getDescripcion());
        juego.setAnioLanzamiento(pedido.getAnioLanzamiento());
        juego.setGeneroId(pedido.getGeneroId());
        juego.setPlataformaId(pedido.getPlataformaId());
        juego.setEstudioId(pedido.getEstudioId());

        Juego guardado = repositorio.save(juego);
        return enriquecerJuego(guardado);
    }
    public void borrarjuego(Long id){
        if(!repositorio.existsById(id)){
            throw new RuntimeException("Juego no encontrado con id:"+id);
        }
        repositorio.deleteById(id);
    }
//aqui hice el mapeo de juego llamando a los demas servicios//
    public JuegoRespuestaDTO enriquecerJuego(Juego juego){

        GeneroDTO genero = generowebClient.get()
        .uri("/api/v1/generos/{id}",juego.getGeneroId())
        .retrieve()
        .onStatus(status->status.is4xxClientError(),
         response-> response.bodyToMono(String.class)
        .map(body -> new RuntimeException("Genero con ID"+ juego.getGeneroId()+"no encontrado en genero Servicio"
    ))
)
    .bodyToMono(GeneroDTO.class)
    .onErrorReturn(new GeneroDTO())
    .block();


    PlataformaDTO plataforma = plataformawebClient.get()
                .uri("/api/v1/platforma/{id}", juego.getPlataformaId())
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError(),
                    response -> response.bodyToMono(String.class)
                        .map(body -> new RuntimeException(
                            "Plataforma con ID " + juego.getPlataformaId() + " no encontrada en Platforma Service"
                        ))
                )
                .bodyToMono(PlataformaDTO.class)
                .onErrorReturn(new PlataformaDTO())
                .block();

    EstudioDTO estudio = estudiowebClient.get()
                .uri("/api/studios/{id}", juego.getEstudioId())
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError(),
                    response -> response.bodyToMono(String.class)
                        .map(body -> new RuntimeException(
                            "Estudio con ID " + juego.getEstudioId() + " no encontrado en Estudio Service"
                        ))
                )
                .bodyToMono(EstudioDTO.class)
                .onErrorReturn(new EstudioDTO())
                .block();

    JuegoRespuestaDTO respuesta = new JuegoRespuestaDTO();
        respuesta.setId(juego.getId());
        respuesta.setTitulo(juego.getTitulo());
        respuesta.setDescripcion(juego.getDescripcion());
        respuesta.setAnioLanzamiento(juego.getAnioLanzamiento());
        respuesta.setGenero(genero);
        respuesta.setPlataforma(plataforma);
        respuesta.setEstudio(estudio);

        return respuesta;
    }
    private void validarIdExterno(Long generoId, Long plataformaId, Long estudioId){
        try {
            generowebClient.get()
            .uri("/api/v1/genero/{id}",generoId)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(),
             response->response.bodyToMono(String.class)
            .map(b-> new RuntimeException("Genero id"+generoId +"no existe")))
            .bodyToMono(GeneroDTO.class)
            .block();

            plataformawebClient.get()
            .uri("/api/v1/plataforma/{id}",plataformaId)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(),
             response->response.bodyToMono(String.class)
            .map(b-> new RuntimeException("Plataforma id"+plataformaId +"no existe")))
            .bodyToMono(PlataformaDTO.class)
            .block();
            
            estudiowebClient.get()
                .uri("/api/v1/estudio/{id}", estudioId)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                    response -> response.bodyToMono(String.class)
                        .map(b -> new RuntimeException("Estudio ID " + estudioId + " no existe")))
                .bodyToMono(EstudioDTO.class)
                .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al validar IDs externos: " + e.getMessage(), e);
        }
    }
    public JuegoRespuestaDTO actualizar(Long id, JuegoPedidoDTO pedido) {
    Juego juego = repositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Juego no encontrado con ID: " + id));

    juego.setTitulo(pedido.getTitulo());
    juego.setDescripcion(pedido.getDescripcion());
    juego.setAnioLanzamiento(pedido.getAnioLanzamiento());
    juego.setGeneroId(pedido.getGeneroId());
    juego.setPlataformaId(pedido.getPlataformaId());
    juego.setEstudioId(pedido.getEstudioId());

    Juego actualizado = repositorio.save(juego);
    return enriquecerJuego(actualizado);
}

}
