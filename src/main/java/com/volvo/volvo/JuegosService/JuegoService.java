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


    //------son los Beans creados en webclient//
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
//---------Obtener por id usando El DTO--------//
    public JuegoRespuestaDTO obtenerJuegoporId(Long id){
        Juego juego = repositorio.findById(id)
                    .orElseThrow(()-> new RuntimeException("Juego no encontrado con id;"+id));
        return enriquecerJuego(juego);            
    }

    ///--------CREAR JUego-------//

    public JuegoRespuestaDTO crearJuego(JuegoPedidoDTO pedido){

        //validamos que los id existen en sus respectivos servicios
        validarIdExterno(pedido.getGeneroid(), pedido.getPlataformaid(), pedido.getEstudioid());

        Juego juego = new Juego();
        juego.setTitulo(pedido.getTitulo());
        juego.setDescripcion(pedido.getDescripcion());
        juego.setAnioLanzamiento(pedido.getAnioLanzamiento());
        juego.setGeneroid(pedido.getGeneroid());
        juego.setPlataformaid(pedido.getPlataformaid());
        juego.setEstudioid(pedido.getEstudioid());

        Juego guardado = repositorio.save(juego);
        return enriquecerJuego(guardado);
    }
    public void borrarjuego(Long id){
        if(!repositorio.existsById(id)){
            throw new RuntimeException("Juego no encontrado con id:"+id);
        }
        repositorio.deleteById(id);
    }

    //-----Metodo exclusivo: metodo para enriquecer un Juego con los datos de otros
    //servicios segun Webclient. recibe Entidad Juego que solo tiene los ids 
    //para devolver un JuegoRespuestaDTO que tiene los objetos completos
    private JuegoRespuestaDTO enriquecerJuego(Juego juego){

        //Llama al genero sercicio con un Get localhost:8081/api/v1/...
        GeneroDTO genero = generowebClient.get()
        .uri("/api/v1/generos/{id}",juego.getGeneroid())
        .retrieve()
        .onStatus(status->status.is4xxClientError(),
         response-> response.bodyToMono(String.class)
        .map(body -> new RuntimeException("Genero con ID"+ juego.getGeneroid()+"no encontrado en genero Servicio"
    ))
)
    .bodyToMono(GeneroDTO.class).block();


    PlataformaDTO plataforma = plataformawebClient.get()
                .uri("/api/v1/platforma/{id}", juego.getPlataformaid())
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError(),
                    response -> response.bodyToMono(String.class)
                        .map(body -> new RuntimeException(
                            "Plataforma con ID " + juego.getPlataformaid() + " no encontrada en Platforma Service"
                        ))
                )
                .bodyToMono(PlataformaDTO.class)
                .block();

    EstudioDTO estudio = estudiowebClient.get()
                .uri("/api/studios/{id}", juego.getEstudioid())
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError(),
                    response -> response.bodyToMono(String.class)
                        .map(body -> new RuntimeException(
                            "Estudio con ID " + juego.getEstudioid() + " no encontrado en Estudio Service"
                        ))
                )
                .bodyToMono(EstudioDTO.class)
                .block();

    JuegoRespuestaDTO respuesta = new JuegoRespuestaDTO();
        respuesta.setId(juego.getId());
        respuesta.setTitulo(juego.getTitulo());
        respuesta.setDescipcion(juego.getDescripcion());
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
                .uri("/api/estudio/{id}", estudioId)
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

}
