package com.cooperativa.votacao.integration;

import com.cooperativa.votacao.domain.entity.*;
import com.cooperativa.votacao.domain.enums.OpcaoVoto;
import com.cooperativa.votacao.domain.repository.*;
import com.cooperativa.votacao.dto.request.VotoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql(
        statements = {
                "TRUNCATE TABLE votos RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE sessoes RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE pautas RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE associado_assuntos RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE associados RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE assuntos RESTART IDENTITY CASCADE"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class VotoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Test
    void deveRegistrarVoto() throws Exception {

        Dados dados = criarDadosBase();

        VotoRequest request = new VotoRequest(
                dados.associado().getId(),
                OpcaoVoto.SIM
        );

        mockMvc.perform(
                        post("/sessoes/{sessaoId}/votos", dados.sessao().getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        assertEquals(1, votoRepository.findAll().size());
    }

    @Test
    void naoDevePermitirVotoDuplicadoNaMesmaSessao() throws Exception {

        Dados dados = criarDadosBase();

        VotoRequest request = new VotoRequest(
                dados.associado().getId(),
                OpcaoVoto.SIM
        );

        mockMvc.perform(
                        post("/sessoes/{sessaoId}/votos", dados.sessao().getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        post("/sessoes/{sessaoId}/votos", dados.sessao().getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Associado já votou nessa pauta"));
    }

    @Test
    void naoDevePermitirVotoDeAssociadoSemAssuntoDaPauta() throws Exception {

        Dados dados = criarDadosBase();

        Associado associadoSemPermissao = associadoRepository.save(
                Associado.builder()
                        .id(UUID.randomUUID())
                        .associadoCodigo(2)
                        .nome("João")
                        .cpf("12345678902")
                        .endereco("Rua 2")
                        .associadoEm(LocalDateTime.now())
                        .assuntos(new HashSet<>())
                        .build()
        );

        VotoRequest request = new VotoRequest(
                associadoSemPermissao.getId(),
                OpcaoVoto.SIM
        );

        mockMvc.perform(
                        post("/sessoes/{sessaoId}/votos", dados.sessao().getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Associado não pode votar nessa pauta"));
    }

    private Dados criarDadosBase() {

        Assunto assunto = assuntoRepository.save(
                Assunto.builder()
                        .id(UUID.randomUUID())
                        .assuntoCodigo(1)
                        .nome("Tecnologia")
                        .build()
        );

        Associado associado = Associado.builder()
                .id(UUID.randomUUID())
                .associadoCodigo(1)
                .nome("Maria")
                .cpf("12345678901")
                .endereco("Rua 1")
                .associadoEm(LocalDateTime.now())
                .assuntos(new HashSet<>(Set.of(assunto)))
                .build();

        associadoRepository.save(associado);

        Pauta pauta = pautaRepository.save(
                Pauta.builder()
                        .id(UUID.randomUUID())
                        .titulo("Pauta teste")
                        .descricao("Descrição teste")
                        .criadaEm(LocalDateTime.now())
                        .assunto(assunto)
                        .build()
        );

        Sessao sessao = sessaoRepository.save(
                Sessao.builder()
                        .id(UUID.randomUUID())
                        .pauta(pauta)
                        .inicio(LocalDateTime.now().minusMinutes(1))
                        .fim(LocalDateTime.now().plusMinutes(5))
                        .duracaoEmMinutos(5)
                        .build()
        );

        return new Dados(sessao, associado);
    }

    private record Dados(
            Sessao sessao,
            Associado associado
    ) {
    }
}