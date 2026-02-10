package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.respuesta.*;
import com.aluracursos.forohub.domain.topico.TopicoRepository;
import com.aluracursos.forohub.domain.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/respuestas")
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroRespuesta datos) {

        var topico = topicoRepository.findById(datos.idTopico())
                .orElseThrow(() -> new RuntimeException("Tópico no encontrado"));

        var autor = usuarioRepository.findById(datos.idAutor())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var respuesta = new Respuesta(datos.mensaje(), topico, autor);
        respuestaRepository.save(respuesta);

        return ResponseEntity.ok(new DatosDetalleRespuesta(respuesta));
    }

    @GetMapping
    public ResponseEntity<Page<DatosDetalleRespuesta>> listar(@PageableDefault(size = 10) Pageable paginacion) {
        return ResponseEntity.ok(respuestaRepository.findAll(paginacion).map(DatosDetalleRespuesta::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity detallar(@PathVariable Long id) {
        var respuesta = respuestaRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosDetalleRespuesta(respuesta));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizacionRespuesta datos) {
        var respuesta = respuestaRepository.getReferenceById(id);
        respuesta.actualizarMensaje(datos.mensaje());
        return ResponseEntity.ok(new DatosDetalleRespuesta(respuesta));
    }

    @PutMapping("/{id}/solucion")
    @Transactional
    public ResponseEntity marcarComoSolucion(@PathVariable Long id, Principal principal) {
        var respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        var topico = respuesta.getTopico();

        String loginAutorTopico = topico.getAutor().getLogin();
        String usuarioLogueado = principal.getName();

        if (!loginAutorTopico.equals(usuarioLogueado)) {
            throw new ValidationException("Solo el autor del tópico puede marcar una respuesta como solución");
        }

        respuesta.marcarComoSolucion();
        topico.marcarComoSolucionado();

        return ResponseEntity.ok(new DatosDetalleRespuesta(respuesta));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminar(@PathVariable Long id) {
        var respuesta = respuestaRepository.getReferenceById(id);
        respuestaRepository.delete(respuesta);
        return ResponseEntity.noContent().build();
    }
}