CREATE TABLE cursos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    activo TINYINT NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE topicos DROP COLUMN autor;
ALTER TABLE topicos DROP COLUMN curso;

ALTER TABLE topicos ADD autor_id BIGINT NOT NULL;
ALTER TABLE topicos ADD curso_id BIGINT NOT NULL;

ALTER TABLE topicos ADD CONSTRAINT fk_topicos_autor_id FOREIGN KEY (autor_id) REFERENCES usuarios(id);
ALTER TABLE topicos ADD CONSTRAINT fk_topicos_curso_id FOREIGN KEY (curso_id) REFERENCES cursos(id);