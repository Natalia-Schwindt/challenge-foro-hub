package com.aluracursos.forohub.domain.respuesta;

import jakarta.validation.constraints.NotBlank;

public record DatosActualizacionRespuesta(@NotBlank String mensaje) {

}
