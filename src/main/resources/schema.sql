DROP TABLE IF EXISTS juegos;

CREATE TABLE juegos (
    id                BIGINT          NOT NULL AUTO_INCREMENT,
    titulo            VARCHAR(200)    NOT NULL,
    descripcion       VARCHAR(1000),
    anio_lanzamiento  INT,
    genero_id         BIGINT          NOT NULL,
    plataforma_id     BIGINT          NOT NULL,
    estudio_id        BIGINT          NOT NULL,

    CONSTRAINT pk_juegos PRIMARY KEY (id)
);