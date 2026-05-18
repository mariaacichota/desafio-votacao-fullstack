# Visão Geral 

Pensei em seguir conceitos próximos do Driven-Domain Design, com o domínio principal sendo os votos nas pautas. Partindo disso, projeto utiliza uma arquitetura em camadas, com separação entre controllers, services, repositories, DTOs, mappers, validators e entidades de domínio. Com base no que foi solicitado nas regras de negócio, então ficariam entidades simples "Pauta", "Voto" e "Sessão", com elas, já seria possível fazer um projeto totalmente funcional. Entretanto, pensando em fazer algo mais completo, adicionei novas entidades ao projeto.

## Regras de Negócio

Com base no que havia sido expecificado como necessidade no README.md:
1. Cadastrar uma nova pauta;
2. Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada de abertura ou 1 minuto por default);
3. Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta);
4. Contabilizar os votos e dar o resultado da votação na pauta.

Para deixar o projeto mais completo, partindo das regras de negócio anteriores, estabeleci duas novas principais:
1. Uma pauta pode ter várias sessões, logo o voto será por sessão;
2. Somente os associados que tem relação com o assunto da pauta podem votar.

Isso porquê: ao invés de a pauta guardar o voto, coloquei essa responsabilidade na sessão, pensando que pautas com o resultado empatado podem ser interessantes de haver novas sessões de desempate; somente pessoas relacionadas ao assunto da pauta podem votar na sessão, já que se não entendo sobre Engenharia de Alimentos, talvez meu voto final acabe sendo incorreto.

## Entidades 

Com base nas regras de negócio aplicadas, então as entidades finais ficaram:
1. Pauta: tabela para guardar as pautas
2. Sessao: tabela para guardar as sessões de voto por pauta;
3. Voto: tabela para guardar os votos das sessões;
4. Associado: tabela para guardar as informações de associados (pode ser importante para realizar a tarefa bônus de validação de CPF válido);
5. Assunto: tabela para guardar tipo do assunto a ser debatido na pauta, pode ser usado futuramente para separar usuários por grupos).

## Enumeradores

Para que não hajam opções erradas/improváveis de voto, ou de resultado, então criei enumeradores para eles:
1. OpcaoVoto: com as opções de 'SIM' ou 'NAO', conforme solicitado no README, mas talvez seja pertinente criar 'NULO';
2. ResultadoVotacao: com as opções de 'APROVADA', 'REPROVADA' e 'EMPATE'.

## Banco de Dados

Para a criação do banco de dados, utilizei o Docker e o Flyway. O Flyway é responsável por fazer as migrations:

1. Migration para criação de tabelas:
```bash
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
```

2. Migration para popular as tabelas para teste:
```bash
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
```

_Observação: popular a tabela de associados via API requer que os CPFs sejam válidos, com base na tarefa bônus._

Para iniciar o Docker: `docker compose up -d`
Para derrubar o Docker: `docker compose up -d`
Para validar que o Docker subiu: `docker ps` (devem estar com o status Up)

## Testes

Dos testes unitários e de integração, tentei fazer algo que cobrisse grande parte do código, entretanto, como ficou algo extenso, aumentar a cobertura acaba requerendo alguns testes que poderiam ser desnecessários ou redundantes. No JaCoCo, a versão final do relatório diz que houve a cobertura de 84%:
<img width="1403" height="310" alt="image" src="https://github.com/user-attachments/assets/1b24cbb2-4cf1-4401-ba05-7cc95c466e34" />

## SwaggerUI e Endpoints

Localmente acessei os endpoints finais via SwaggerUI: http://localhost:8080/swagger-ui/index.html
Os endpoints finais ficaram como:

1. Endpoint de Pautas:
`POST /pautas`: para criar uma nova pauta;
`GET /pautas`: para trazer todas as pautas detalhadas;
`GET /pautas/{pautaId}`: para trazer o detalhe de uma pauta específica.

2. Endpoint de Sessões:
`POST /pautas/{pautaId}/sessoes`: para abrir uma nova sessão de votação para uma pauta;
`GET /pautas/{pautaId}/sessoes`: para listar todas as sessões de uma pauta;
`GET /pautas/{pautaId}/sessoes/{sessaoId}`: para buscar uma sessão específica de uma pauta.

3. Endpoint de Votos:
`POST /sessoes/{sessaoId}/votos`: para registrar o voto de um associado em uma sessão;
`GET /sessoes/{sessaoId}/votos`: para listar os votos registrados em uma sessão.

4. Endpoint de Resultado:
`GET /sessoes/{sessaoId}/resultado`: para contabilizar os votos e retornar o resultado da sessão.

5. Endpoint de Assuntos:
`POST /assuntos`: para criar um novo assunto;
`GET /assuntos`: para listar todos os assuntos cadastrados.

6. Endpoint de Associados:
`POST /associados`: para criar um novo associado;
`GET /associados`: para listar todos os associados cadastrados;
`GET /associados/{associadoId}`: para buscar os dados de um associado específico;
`POST /associados/{associadoId}/assuntos/{assuntoId}`: para vincular um associado a um assunto.

Sobre o versionamento da API, acredito que uma forma simples e viável seja a partir da URI, fazendo uma sequência como: `/api/v1`, `/api/v2`, e assim por diante.
Para assegurar a performance da API (e do projeto como um todo) seria interessante fazer alguns testes de performance futuramente, talvez algo simples com `k6`, do Grafana, na versão gratuita e open-source. Os testes avaliariam tempo de resposta, taxa de erro, concorrência, consistência do voto único e tempo de cálculo do resultado. 

## Melhorias Futuras

Acredito que nessa aplicação há muito que pode melhorar e ser implementado que julgo interessante:
1. "Enxugar" mais o código, refatorando;
2. Melhorar o layout, fazer algo mais profissional e único (fazendo telas com UI/UX para planejar melhor, nova escolha de cores, etc);
2.1. ainda na questão de layout, fazer arquivos separados para os css (que por ora estão in-line);
3. Projetar e implementar telas de criação de Associados e de Assuntos (que devem poder ser vinculados através do front);
4. Criar uma cópia da estrutura do banco de dados para teste (pois alguns zeram todos os valores do banco);
5. Implementar testes de performance com o Grafana;
6. Fazer o versionamento da API para não afetar a versão funcional durante a refatoração.
