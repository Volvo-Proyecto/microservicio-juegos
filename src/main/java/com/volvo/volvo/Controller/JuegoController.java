package com.volvo.volvo.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.volvo.volvo.DTO.JuegoPedidoDTO;
import com.volvo.volvo.DTO.JuegoRespuestaDTO;
import com.volvo.volvo.JuegosService.JuegoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<JuegoRespuestaDTO>> listarTodos() {
        return ResponseEntity.ok(juegoService.obtenerJuegos());
    }
    @GetMapping("/{id}")
    public ResponseEntity<JuegoRespuestaDTO> obtenerporId(@PathVariable Long id) {
        return ResponseEntity.ok(juegoService.obtenerJuegoporId(id));
    }
    @PostMapping
    public ResponseEntity<JuegoRespuestaDTO> crearJuego(@Valid @RequestBody JuegoPedidoDTO nuevo) {
        JuegoRespuestaDTO creado = juegoService.crearJuego(nuevo);

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
    @PutMapping("/{id}")
    public ResponseEntity<JuegoRespuestaDTO> actualizarJuego(@PathVariable Long id,
         @Valid @RequestBody JuegoPedidoDTO nuevo) {
        
        return ResponseEntity.ok(juegoService.actualizar(id, nuevo));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarJuego(@PathVariable Long id){
        juegoService.borrarjuego(id);
        return ResponseEntity.noContent().build();

        
    }
    
    
    

}
