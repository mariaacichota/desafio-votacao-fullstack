package com.cooperativa.votacao.integration;

import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.domain.repository.*;
import com.cooperativa.votacao.dto.request.PautaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
class PautaIntegrationTest {

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
    void deveCriarPauta() throws Exception {

        Assunto assunto = assuntoRepository.save(
                Assunto.builder()
                        .id(UUID.randomUUID())
                        .assuntoCodigo(1)
                        .nome("Tecnologia")
                        .build()
        );

        PautaRequest request = new PautaRequest(
                "Nova pauta",
                "Descrição da pauta",
                assunto.getId()
        );

        mockMvc.perform(
                        post("/pautas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Nova pauta"))
                .andExpect(jsonPath("$.descricao").value("Descrição da pauta"));
    }

    @Test
    void deveListarPautas() throws Exception {

        Assunto assunto = assuntoRepository.save(
                Assunto.builder()
                        .id(UUID.randomUUID())
                        .assuntoCodigo(1)
                        .nome("Tecnologia")
                        .build()
        );

        PautaRequest request = new PautaRequest(
                "Pauta listagem",
                "Descrição listagem",
                assunto.getId()
        );

        mockMvc.perform(
                        post("/pautas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());

        mockMvc.perform(get("/pautas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Pauta listagem"));
    }
}