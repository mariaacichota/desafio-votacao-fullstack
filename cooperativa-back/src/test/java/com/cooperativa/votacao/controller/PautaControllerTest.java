package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.dto.request.PautaRequest;
import com.cooperativa.votacao.dto.response.PautaDetalhadaResponse;
import com.cooperativa.votacao.dto.response.PautaResponse;
import com.cooperativa.votacao.exception.NotFoundException;
import com.cooperativa.votacao.service.PautaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PautaController.class)
@AutoConfigureMockMvc(addFilters = false)
class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PautaService service;

    @Test
    void deveCriarPauta() throws Exception {

        UUID pautaId = UUID.randomUUID();
        UUID assuntoId = UUID.randomUUID();

        PautaRequest request = new PautaRequest(
                "Nova pauta",
                "Descrição da pauta",
                assuntoId
        );

        PautaResponse response = new PautaResponse(
                pautaId,
                "Nova pauta",
                "Descrição da pauta",
                LocalDateTime.now()
        );

        when(service.criar(any(PautaRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/pautas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(pautaId.toString()))
                .andExpect(jsonPath("$.titulo").value("Nova pauta"))
                .andExpect(jsonPath("$.descricao").value("Descrição da pauta"));
    }

    @Test
    void deveListarPautas() throws Exception {

        UUID pautaId = UUID.randomUUID();

        PautaDetalhadaResponse response = new PautaDetalhadaResponse(
                pautaId,
                "Pauta teste",
                "Descrição teste",
                LocalDateTime.now(),
                List.of()
        );

        when(service.buscarTodasPautas())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/pautas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pautaId.toString()))
                .andExpect(jsonPath("$[0].titulo").value("Pauta teste"))
                .andExpect(jsonPath("$[0].descricao").value("Descrição teste"));
    }

    @Test
    void deveBuscarPautaDetalhadaPorId() throws Exception {

        UUID pautaId = UUID.randomUUID();

        PautaDetalhadaResponse response = new PautaDetalhadaResponse(
                pautaId,
                "Pauta detalhe",
                "Descrição detalhe",
                LocalDateTime.now(),
                List.of()
        );

        when(service.buscarDetalhada(pautaId))
                .thenReturn(response);

        mockMvc.perform(get("/pautas/{pautaId}", pautaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pautaId.toString()))
                .andExpect(jsonPath("$.titulo").value("Pauta detalhe"));
    }

    @Test
    void deveRetornar404QuandoPautaNaoExistir() throws Exception {

        UUID pautaId = UUID.randomUUID();

        when(service.buscarDetalhada(pautaId))
                .thenThrow(new NotFoundException("Pauta não encontrada"));

        mockMvc.perform(get("/pautas/{pautaId}", pautaId))
                .andExpect(status().isNotFound());
    }
}