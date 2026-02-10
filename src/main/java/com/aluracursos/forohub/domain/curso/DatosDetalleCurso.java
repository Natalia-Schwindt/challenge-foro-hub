package com.aluracursos.forohub.domain.curso;

public record DatosDetalleCurso(Long id, String nombre, Categoria categoria, Boolean activo) {
    public DatosDetalleCurso(Curso curso) {
        this(curso.getId(), curso.getNombre(), curso.getCategoria(), curso.getActivo());
    }
}