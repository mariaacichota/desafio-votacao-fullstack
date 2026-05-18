package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.domain.entity.Associado;
import com.cooperativa.votacao.service.AssociadoService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssociadoController.class)
@AutoConfigureMockMvc(addFilters = false)
class AssociadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssociadoService service;

    @Test
    void deveCriarAssociado() throws Exception {

        Associado request = Associado.builder()
                .nome("Maria")
                .cpf("12345678901")
                .endereco("Rua Teste")
                .build();

        Associado response = Associado.builder()
                .id(UUID.randomUUID())
                .nome("Maria")
                .cpf("12345678901")
                .endereco("Rua Teste")
                .associadoEm(LocalDateTime.now())
                .build();

        when(service.criar(any(Associado.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/associados")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Maria"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    void deveListarAssociados() throws Exception {

        Associado associado = Associado.builder()
                .id(UUID.randomUUID())
                .nome("Maria")
                .cpf("12345678901")
                .endereco("Rua Teste")
                .associadoEm(LocalDateTime.now())
                .build();

        when(service.listar())
                .thenReturn(List.of(associado));

        mockMvc.perform(get("/associados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Maria"))
                .andExpect(jsonPath("$[0].cpf").value("12345678901"));
    }

    @Test
    void deveBuscarAssociadoPorId() throws Exception {

        UUID associadoId = UUID.randomUUID();

        Associado associado = Associado.builder()
                .id(associadoId)
                .nome("Maria")
                .cpf("12345678901")
                .endereco("Rua Teste")
                .associadoEm(LocalDateTime.now())
                .build();

        when(service.buscarPorId(associadoId))
                .thenReturn(associado);

        mockMvc.perform(get("/associados/{associadoId}", associadoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(associadoId.toString()))
                .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    void deveVincularAssuntoAoAssociado() throws Exception {

        UUID associadoId = UUID.randomUUID();
        UUID assuntoId = UUID.randomUUID();

        doNothing()
                .when(service)
                .vincularAssunto(associadoId, assuntoId);

        mockMvc.perform(
                        post(
                                "/associados/{associadoId}/assuntos/{assuntoId}",
                                associadoId,
                                assuntoId
                        )
                )
                .andExpect(status().isOk());
    }
}