package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.domain.enums.ResultadoVotacao;
import com.cooperativa.votacao.dto.request.SessaoRequest;
import com.cooperativa.votacao.dto.response.SessaoResponse;
import com.cooperativa.votacao.exception.NotFoundException;
import com.cooperativa.votacao.service.SessaoService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class SessaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SessaoService service;

    @Test
    void deveAbrirSessao() throws Exception {

        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();

        SessaoRequest request = new SessaoRequest(5L);

        SessaoResponse response = new SessaoResponse(
                sessaoId,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                5L,
                null,
                true
        );

        when(service.abrirSessao(eq(pautaId), any(SessaoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/pautas/{pautaId}/sessoes", pautaId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessaoId.toString()))
                .andExpect(jsonPath("$.duracaoEmMinutos").value(5))
                .andExpect(jsonPath("$.aberta").value(true));
    }

    @Test
    void deveListarSessoesPorPauta() throws Exception {

        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();

        SessaoResponse response = new SessaoResponse(
                sessaoId,
                LocalDateTime.now().minusMinutes(10),
                LocalDateTime.now().minusMinutes(5),
                5L,
                ResultadoVotacao.APROVADA,
                false
        );

        when(service.listarPorPauta(pautaId))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/pautas/{pautaId}/sessoes", pautaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sessaoId.toString()))
                .andExpect(jsonPath("$[0].resultado").value("APROVADA"))
                .andExpect(jsonPath("$[0].aberta").value(false));
    }

    @Test
    void deveBuscarSessao() throws Exception {

        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();

        SessaoResponse response = new SessaoResponse(
                sessaoId,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                1L,
                null,
                true
        );

        when(service.buscar(pautaId, sessaoId))
                .thenReturn(response);

        mockMvc.perform(
                        get(
                                "/pautas/{pautaId}/sessoes/{sessaoId}",
                                pautaId,
                                sessaoId
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessaoId.toString()))
                .andExpect(jsonPath("$.aberta").value(true));
    }

    @Test
    void deveRetornar404QuandoSessaoNaoExistir() throws Exception {

        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();

        when(service.buscar(pautaId, sessaoId))
                .thenThrow(new NotFoundException("Sessão não encontrada"));

        mockMvc.perform(
                        get(
                                "/pautas/{pautaId}/sessoes/{sessaoId}",
                                pautaId,
                                sessaoId
                        )
                )
                .andExpect(status().isNotFound());
    }
}