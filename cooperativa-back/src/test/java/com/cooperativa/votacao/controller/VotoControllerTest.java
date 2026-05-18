package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.domain.entity.Voto;
import com.cooperativa.votacao.domain.enums.OpcaoVoto;
import com.cooperativa.votacao.dto.request.VotoRequest;
import com.cooperativa.votacao.exception.BusinessException;
import com.cooperativa.votacao.service.VotoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VotoController.class)
@AutoConfigureMockMvc(addFilters = false)
class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VotoService service;

    @Test
    void deveRegistrarVoto() throws Exception {

        UUID sessaoId = UUID.randomUUID();

        VotoRequest request = new VotoRequest(
                UUID.randomUUID(),
                OpcaoVoto.SIM
        );

        mockMvc.perform(
                        post("/sessoes/{sessaoId}/votos", sessaoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar400QuandoAssociadoJaVotou() throws Exception {

        UUID sessaoId = UUID.randomUUID();

        VotoRequest request = new VotoRequest(
                UUID.randomUUID(),
                OpcaoVoto.SIM
        );

        doThrow(new BusinessException("Associado já votou nessa pauta"))
                .when(service)
                .votar(eq(sessaoId), any(VotoRequest.class));

        mockMvc.perform(
                        post("/sessoes/{sessaoId}/votos", sessaoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarVotosDaSessao() throws Exception {

        UUID sessaoId = UUID.randomUUID();

        Voto voto = Voto.builder()
                .id(UUID.randomUUID())
                .opcao(OpcaoVoto.SIM)
                .build();

        when(service.listarVotos(sessaoId))
                .thenReturn(List.of(voto));

        mockMvc.perform(get("/sessoes/{sessaoId}/votos", sessaoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].opcao").value("SIM"));
    }
}