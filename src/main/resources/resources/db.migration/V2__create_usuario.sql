CREATE TABLE IF NOT EXISTS usuario (
    id            BIGSERIAL PRIMARY KEY,
    tipo_usuario  VARCHAR(31)  NOT NULL,
    nome          VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    senha         VARCHAR(255) NOT NULL
);
