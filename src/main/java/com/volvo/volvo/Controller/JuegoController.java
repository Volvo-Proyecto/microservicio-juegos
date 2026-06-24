package com.volvo.volvo.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.volvo.volvo.Assembler.JuegoClassAssembler;
import com.volvo.volvo.DTO.JuegoPedidoDTO;
import com.volvo.volvo.DTO.JuegoRespuestaDTO;
import com.volvo.volvo.JuegosService.JuegoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/juegos")
@RequiredArgsConstructor
public class JuegoController {
    
    private final JuegoService juegoService;
    private final JuegoClassAssembler assembler;

    @Operation(
        summary = "Listar todos los juegos",
        description = "Devuelve la lista completa de videojuegos registrados en el catálogo con sus respectivos géneros, plataformas y estudios."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista obtenida correctamente"
    )
    @GetMapping
    public ResponseEntity<List<JuegoRespuestaDTO>> listarTodos() {
        return ResponseEntity.ok(juegoService.obtenerJuegos());
    }

    @Operation(
        summary = "Buscar Juego por ID", 
        description = "Obtiene un juego específico buscándolo por su identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Juego encontrado"),
        @ApiResponse(responseCode = "404", description = "Juego no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<JuegoRespuestaDTO>> obtenerporId(
            @Parameter(description = "ID del juego a buscar", example = "1") 
            @PathVariable Long id) {
        JuegoRespuestaDTO j = juegoService.obtenerJuegoporId(id);
        return new ResponseEntity<>(assembler.toModel(j), HttpStatus.OK);
    }

    @Operation(
        summary = "Crear un nuevo juego",
        description = "Registra un juego nuevo en el catálogo asociándolo a un género, plataforma y estudio existentes."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Juego creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud (ej. campos vacíos)")
    })
    @PostMapping
    public ResponseEntity<JuegoRespuestaDTO> crearJuego(@Valid @RequestBody JuegoPedidoDTO nuevo) {
        JuegoRespuestaDTO creado = juegoService.crearJuego(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(
        summary = "Actualizar un juego existente",
        description = "Modifica los detalles de un juego previamente registrado según su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Juego actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Juego no encontrado para actualizar")
    })
    @PutMapping("/{id}")
    public ResponseEntity<JuegoRespuestaDTO> actualizarJuego(
            @Parameter(description = "ID del juego a actualizar", example = "1") @PathVariable Long id,
            @Valid @RequestBody JuegoPedidoDTO nuevo) {
        return ResponseEntity.ok(juegoService.actualizar(id, nuevo));
    }

    @Operation(
        summary = "Eliminar un juego",
        description = "Elimina un juego del catálogo de forma permanente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Juego eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Juego no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarJuego(
            @Parameter(description = "ID del juego a eliminar", example = "1") @PathVariable Long id) {
        juegoService.borrarjuego(id);
        return ResponseEntity.noContent().build();
    }
}