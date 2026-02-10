package com.aluracursos.forohub.domain.respuesta;

import java.time.LocalDateTime;

public record DatosDetalleRespuesta(
        Long id,
        String mensaje,
        Long idTopico,
        Long idAutor,
        LocalDateTime fechaCreacion,
        Boolean solucion
) {
    public DatosDetalleRespuesta(Respuesta respuesta) {
        this(respuesta.getId(), respuesta.getMensaje(),
                respuesta.getTopico().getId(),
                respuesta.getAutor().getId(),
                respuesta.getFechaCreacion(),
                respuesta.getSolucion());
    }
}