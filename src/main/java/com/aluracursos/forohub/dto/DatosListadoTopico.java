package com.aluracursos.forohub.dto;

import com.aluracursos.forohub.domain.topico.StatusTopico;
import com.aluracursos.forohub.domain.topico.Topico;
import java.time.LocalDateTime;

public record DatosListadoTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        StatusTopico status,
        String autor,
        String curso
) {
    public DatosListadoTopico(Topico topico) {
        this(topico.getId(), topico.getTitulo(), topico.getMensaje(),
                topico.getFechaCreacion(), topico.getStatus(),
                topico.getAutor(), topico.getCurso());
    }
}