package com.aluracursos.forohub.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);
    @Query("""
           SELECT t FROM Topico t 
           WHERE t.curso = :nombreCurso 
           AND YEAR(t.fechaCreacion) = :anio
           """)
    Page<Topico> findByCursoYAnio(String nombreCurso, int anio, Pageable paginacion);
}