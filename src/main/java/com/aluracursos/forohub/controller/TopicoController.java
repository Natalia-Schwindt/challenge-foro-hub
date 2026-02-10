package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.topico.Topico;
import com.aluracursos.forohub.domain.topico.TopicoRepository;
import com.aluracursos.forohub.domain.topico.DatosActualizacionTopico;
import com.aluracursos.forohub.domain.topico.DatosDetalleTopico;
import com.aluracursos.forohub.domain.topico.DatosRegistroTopico;
import com.aluracursos.forohub.domain.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import com.aluracursos.forohub.domain.topico.DatosListadoTopico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import com.aluracursos.forohub.domain.curso.CursoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public TopicoController(TopicoRepository repository, UsuarioRepository usuarioRepository, CursoRepository cursoRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> registrarTopico(@RequestBody @Valid DatosRegistroTopico datos, UriComponentsBuilder uriBuilder) {

        if (repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            return ResponseEntity.badRequest().body("Error: Ya existe un tópico con el mismo título y mensaje.");
        }

        var autor = usuarioRepository.findByNombre(datos.autor());
        var curso = cursoRepository.findByNombre(datos.curso());

        var topico = new Topico(datos, autor, curso);
        repository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosDetalleTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listarTopicos(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer anio,
            @PageableDefault(size = 10, sort = "fechaCreacion", direction = Sort.Direction.ASC) Pageable paginacion) {

        Page<Topico> pagina;

        if (curso != null && anio != null) {
            pagina = repository.findByCursoYAnio(curso, anio, paginacion);
        } else {
            pagina = repository.findAllByActivoTrue(paginacion);
        }

        return ResponseEntity.ok(pagina.map(DatosListadoTopico::new));
    }

@GetMapping("/{id}")
public ResponseEntity<?> detallar(@PathVariable Long id) {
    return repository.findById(id)
            .map(topico -> ResponseEntity.ok(new DatosDetalleTopico(topico)))
            .orElse(ResponseEntity.notFound().build());
}

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizacionTopico datos) {
        var topicoOptional = repository.findById(id);
        if (topicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var topico = topicoOptional.get();

        if (!topico.getTitulo().equals(datos.titulo()) || !topico.getMensaje().equals(datos.mensaje())) {
            if (repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
                return ResponseEntity.badRequest().body("Error: Ya existe otro tópico con el mismo título y mensaje.");
            }
        }

        topico.actualizarDatos(datos);
        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        var topico = repository.getReferenceById(id);
        topico.eliminar();

        return ResponseEntity.noContent().build();
    }
}