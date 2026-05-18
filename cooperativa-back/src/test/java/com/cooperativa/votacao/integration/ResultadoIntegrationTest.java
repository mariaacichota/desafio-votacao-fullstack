package com.cooperativa.votacao.integration;

import com.cooperativa.votacao.domain.entity.*;
import com.cooperativa.votacao.domain.enums.OpcaoVoto;
import com.cooperativa.votacao.domain.enums.ResultadoVotacao;
import com.cooperativa.votacao.domain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class ResultadoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
    void deveRetornarResultadoAprovado() throws Exception {

        Dados dados = criarDadosBase();

        Associado associado2 = associadoRepository.save(
                Associado.builder()
                        .id(UUID.randomUUID())
                        .associadoCodigo(2)
                        .nome("João")
                        .cpf("12345678902")
                        .endereco("Rua 2")
                        .associadoEm(LocalDateTime.now())
                        .build()
        );

        votoRepository.save(
                Voto.builder()
                        .id(UUID.randomUUID())
                        .sessao(dados.sessao())
                        .associado(dados.associado())
                        .opcao(OpcaoVoto.SIM)
                        .votadoEm(LocalDateTime.now())
                        .build()
        );

        votoRepository.save(
                Voto.builder()
                        .id(UUID.randomUUID())
                        .sessao(dados.sessao())
                        .associado(associado2)
                        .opcao(OpcaoVoto.SIM)
                        .votadoEm(LocalDateTime.now())
                        .build()
        );

        mockMvc.perform(
                        get("/sessoes/{sessaoId}/resultado", dados.sessao().getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votosSim").value(2))
                .andExpect(jsonPath("$.votosNao").value(0))
                .andExpect(jsonPath("$.resultado").value("APROVADA"));
    }

    @Test
    void deveRetornarResultadoEmpate() throws Exception {

        Dados dados = criarDadosBase();

        Associado associado2 = associadoRepository.save(
                Associado.builder()
                        .id(UUID.randomUUID())
                        .associadoCodigo(2)
                        .nome("João")
                        .cpf("12345678902")
                        .endereco("Rua 2")
                        .associadoEm(LocalDateTime.now())
                        .build()
        );

        votoRepository.save(
                Voto.builder()
                        .id(UUID.randomUUID())
                        .sessao(dados.sessao())
                        .associado(dados.associado())
                        .opcao(OpcaoVoto.SIM)
                        .votadoEm(LocalDateTime.now())
                        .build()
        );

        votoRepository.save(
                Voto.builder()
                        .id(UUID.randomUUID())
                        .sessao(dados.sessao())
                        .associado(associado2)
                        .opcao(OpcaoVoto.NAO)
                        .votadoEm(LocalDateTime.now())
                        .build()
        );

        mockMvc.perform(
                        get("/sessoes/{sessaoId}/resultado", dados.sessao().getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votosSim").value(1))
                .andExpect(jsonPath("$.votosNao").value(1))
                .andExpect(jsonPath("$.resultado").value("EMPATE"));
    }

    private Dados criarDadosBase() {

        Assunto assunto = assuntoRepository.save(
                Assunto.builder()
                        .id(UUID.randomUUID())
                        .assuntoCodigo(1)
                        .nome("Tecnologia")
                        .build()
        );

        Associado associado = associadoRepository.save(
                Associado.builder()
                        .id(UUID.randomUUID())
                        .associadoCodigo(1)
                        .nome("Maria")
                        .cpf("12345678901")
                        .endereco("Rua 1")
                        .associadoEm(LocalDateTime.now())
                        .build()
        );

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
                        .inicio(LocalDateTime.now().minusMinutes(10))
                        .fim(LocalDateTime.now().minusMinutes(1))
                        .duracaoEmMinutos(9)
                        .resultado(ResultadoVotacao.APROVADA)
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