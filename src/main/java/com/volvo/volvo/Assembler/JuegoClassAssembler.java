package com.volvo.volvo.Assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.volvo.volvo.Controller.JuegoController;
import com.volvo.volvo.DTO.JuegoRespuestaDTO;


@Component
public class JuegoClassAssembler implements RepresentationModelAssembler<JuegoRespuestaDTO,EntityModel<JuegoRespuestaDTO>>{

    @Override
    public EntityModel<JuegoRespuestaDTO> toModel(JuegoRespuestaDTO j){
       return EntityModel.of(j,
            linkTo(methodOn(JuegoController.class).obtenerporId(j.getId())).withRel("Obtener el Juego")
        );
    }


}
