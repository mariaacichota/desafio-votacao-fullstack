package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.service.AssuntoService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssuntoController.class)
@AutoConfigureMockMvc(addFilters = false)
class AssuntoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssuntoService service;

    @Test
    void deveCriarAssunto() throws Exception {

        Assunto request = Assunto.builder()
                .nome("Tecnologia")
                .build();

        Assunto response = Assunto.builder()
                .id(UUID.randomUUID())
                .nome("Tecnologia")
                .build();

        when(service.criar(any(Assunto.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/assuntos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Tecnologia"));
    }

    @Test
    void deveListarAssuntos() throws Exception {

        Assunto assunto = Assunto.builder()
                .id(UUID.randomUUID())
                .nome("Tecnologia")
                .build();

        when(service.listar())
                .thenReturn(List.of(assunto));

        mockMvc.perform(get("/assuntos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Tecnologia"));
    }
}