package com.aluracursos.forohub.domain.curso;

import jakarta.validation.constraints.NotNull;

public record DatosActualizacionCurso(
        @NotNull
        Long id,
        String nombre,
        Categoria categoria
) {
}