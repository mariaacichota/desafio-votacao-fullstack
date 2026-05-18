package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.domain.enums.ResultadoVotacao;
import com.cooperativa.votacao.dto.response.ResultadoResponse;
import com.cooperativa.votacao.exception.NotFoundException;
import com.cooperativa.votacao.service.ResultadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResultadoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ResultadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResultadoService service;

    @Test
    void deveRetornarResultadoDaSessao() throws Exception {

        UUID sessaoId = UUID.randomUUID();

        ResultadoResponse response = new ResultadoResponse(
                sessaoId,
                4L,
                2L,
                ResultadoVotacao.APROVADA
        );

        when(service.calcularResultado(sessaoId))
                .thenReturn(response);

        mockMvc.perform(get("/sessoes/{sessaoId}/resultado", sessaoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessaoId").value(sessaoId.toString()))
                .andExpect(jsonPath("$.votosSim").value(4))
                .andExpect(jsonPath("$.votosNao").value(2))
                .andExpect(jsonPath("$.resultado").value("APROVADA"));
    }

    @Test
    void deveRetornar404QuandoSessaoNaoExistir() throws Exception {

        UUID sessaoId = UUID.randomUUID();

        when(service.calcularResultado(sessaoId))
                .thenThrow(new NotFoundException("Sessão não encontrada"));

        mockMvc.perform(get("/sessoes/{sessaoId}/resultado", sessaoId))
                .andExpect(status().isNotFound());
    }
}