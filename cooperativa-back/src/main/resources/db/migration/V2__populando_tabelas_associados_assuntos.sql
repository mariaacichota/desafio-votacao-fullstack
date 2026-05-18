INSERT INTO assuntos (
    id,
    assunto_codigo,
    nome
) VALUES
      (
          '11111111-1111-1111-1111-111111111111',
          1,
          'Tecnologia'
      ),
      (
          '22222222-2222-2222-2222-222222222222',
          2,
          'Financeiro'
      ),
      (
          '33333333-3333-3333-3333-333333333333',
          3,
          'Recursos Humanos'
      ),
      (
          '44444444-4444-4444-4444-444444444444',
          4,
          'Jurídico'
      ),
      (
          '55555555-5555-5555-5555-555555555555',
          5,
          'Marketing'
      );

INSERT INTO associados (
    id,
    associado_codigo,
    nome,
    endereco,
    cpf,
    associado_em
) VALUES
      (
          'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
          1001,
          'Maria Souza',
          'Rua das Flores, 100',
          '12345678901',
          CURRENT_TIMESTAMP
      ),
      (
          'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
          1002,
          'João Pereira',
          'Av. Central, 250',
          '12345678902',
          CURRENT_TIMESTAMP
      ),
      (
          'cccccccc-cccc-cccc-cccc-cccccccccccc',
          1003,
          'Ana Lima',
          'Rua do Comércio, 45',
          '12345678903',
          CURRENT_TIMESTAMP
      ),
      (
          'dddddddd-dddd-dddd-dddd-dddddddddddd',
          1004,
          'Carlos Mendes',
          'Rua Aurora, 78',
          '12345678904',
          CURRENT_TIMESTAMP
      ),
      (
          'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
          1005,
          'Fernanda Alves',
          'Av. Paulista, 900',
          '12345678905',
          CURRENT_TIMESTAMP
      );

INSERT INTO associado_assuntos (
    associado_id,
    assunto_id
) VALUES

(
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
    '11111111-1111-1111-1111-111111111111'
),
(
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
    '22222222-2222-2222-2222-222222222222'
),

(
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
    '33333333-3333-3333-3333-333333333333'
),

(
    'cccccccc-cccc-cccc-cccc-cccccccccccc',
    '44444444-4444-4444-4444-444444444444'
),
(
    'cccccccc-cccc-cccc-cccc-cccccccccccc',
    '55555555-5555-5555-5555-555555555555'
),

(
    'dddddddd-dddd-dddd-dddd-dddddddddddd',
    '11111111-1111-1111-1111-111111111111'
),

(
    'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
    '22222222-2222-2222-2222-222222222222'
),
(
    'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
    '55555555-5555-5555-5555-555555555555'
);