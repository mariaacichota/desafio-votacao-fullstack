package com.cooperativa.votacao.integration;

import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.domain.entity.Pauta;
import com.cooperativa.votacao.domain.repository.AssuntoRepository;
import com.cooperativa.votacao.domain.repository.PautaRepository;
import com.cooperativa.votacao.domain.repository.SessaoRepository;
import com.cooperativa.votacao.domain.repository.VotoRepository;
import com.cooperativa.votacao.dto.request.SessaoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class SessaoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private VotoRepository votoRepository;

    @Test
    void deveAbrirSessaoParaPauta() throws Exception {

        Pauta pauta = criarPauta();

        SessaoRequest request = new SessaoRequest(5L);

        mockMvc.perform(
                        post("/pautas/{pautaId}/sessoes", pauta.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.duracaoEmMinutos").value(5))
                .andExpect(jsonPath("$.aberta").value(true));
    }

    @Test
    void naoDeveAbrirSessaoQuandoJaExisteSessaoAberta() throws Exception {

        Pauta pauta = criarPauta();

        SessaoRequest request = new SessaoRequest(5L);

        mockMvc.perform(
                        post("/pautas/{pautaId}/sessoes", pauta.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        post("/pautas/{pautaId}/sessoes", pauta.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarSessoesDaPauta() throws Exception {

        Pauta pauta = criarPauta();

        SessaoRequest request = new SessaoRequest(5L);

        mockMvc.perform(
                        post("/pautas/{pautaId}/sessoes", pauta.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/pautas/{pautaId}/sessoes", pauta.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }

    private Pauta criarPauta() {

        Assunto assunto = assuntoRepository.save(
                Assunto.builder()
                        .id(UUID.randomUUID())
                        .assuntoCodigo(1)
                        .nome("Tecnologia")
                        .build()
        );

        return pautaRepository.save(
                Pauta.builder()
                        .id(UUID.randomUUID())
                        .titulo("Pauta teste")
                        .descricao("Descrição teste")
                        .criadaEm(LocalDateTime.now())
                        .assunto(assunto)
                        .build()
        );
    }
}