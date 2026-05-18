CREATE TABLE assuntos (
                          id UUID PRIMARY KEY,
                          assunto_codigo INTEGER,
                          nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE pautas (
    id UUID PRIMARY KEY,
    assunto_id UUID NOT NULL,
    pauta_codigo INTEGER,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    criada_em TIMESTAMP,

    CONSTRAINT fk_pauta_assunto
        FOREIGN KEY (assunto_id) REFERENCES assuntos(id)
);

CREATE TABLE sessoes (
    id UUID PRIMARY KEY,
    pauta_id UUID NOT NULL,
    sessao_codigo INTEGER,
    inicio TIMESTAMP NOT NULL,
    fim TIMESTAMP NOT NULL,
    duracao_em_minutos INTEGER,
    resultado VARCHAR(20),

    CONSTRAINT fk_pauta
        FOREIGN KEY (pauta_id) REFERENCES pautas(id)
);

CREATE TABLE votos (
    id UUID PRIMARY KEY,
    voto_codigo INTEGER,
    sessao_id UUID NOT NULL,
    associado_id UUID NOT NULL,
    opcao VARCHAR(10) NOT NULL,
    votado_em TIMESTAMP NOT NULL,

    CONSTRAINT fk_voto_sessao
       FOREIGN KEY (sessao_id) REFERENCES sessoes(id),

    CONSTRAINT uk_voto_unico
       UNIQUE (sessao_id, associado_id)
);

CREATE TABLE associados (
    id UUID PRIMARY KEY,
    associado_codigo INTEGER,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    cpf VARCHAR(11),
    associado_em TIMESTAMP NOT NULL
);

CREATE TABLE associado_assuntos (
    associado_id UUID NOT NULL,
    assunto_id UUID NOT NULL,

    PRIMARY KEY (associado_id, assunto_id),

    CONSTRAINT fk_assoc_assunto_associado
        FOREIGN KEY (associado_id) REFERENCES associados(id),

    CONSTRAINT fk_assoc_assunto_assunto
        FOREIGN KEY (assunto_id) REFERENCES assuntos(id)
);