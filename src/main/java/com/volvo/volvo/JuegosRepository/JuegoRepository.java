package com.volvo.volvo.JuegosRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.volvo.volvo.Model.Juego;
import java.util.List;


@Repository
public interface JuegoRepository extends JpaRepository<Juego,Long>{
    List<Juego> findByGeneroId(Long generoId);
    List<Juego> findByEstudioId(Long estudioId);
    List<Juego> findByPlataformaId(Long plataformaId);
    List<Juego> findByTitulo(String titulo);

}
