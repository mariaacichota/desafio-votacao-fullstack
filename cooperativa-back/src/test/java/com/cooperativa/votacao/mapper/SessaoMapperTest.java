package com.cooperativa.votacao.mapper;

import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.domain.enums.ResultadoVotacao;
import com.cooperativa.votacao.dto.response.SessaoResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SessaoMapperTest {

    @Test
    void deveConverterSessaoAbertaParaResponse() {

        UUID sessaoId = UUID.randomUUID();

        LocalDateTime inicio =
                LocalDateTime.now().minusMinutes(1);

        LocalDateTime fim =
                LocalDateTime.now().plusMinutes(4);

        Sessao sessao = Sessao.builder()
                .id(sessaoId)
                .inicio(inicio)
                .fim(fim)
                .duracaoEmMinutos(5)
                .resultado(null)
                .build();

        SessaoResponse response =
                SessaoMapper.toResponse(
                        sessao,
                        null
                );

        assertNotNull(response);
        assertEquals(sessaoId, response.id());
        assertEquals(inicio, response.inicio());
        assertEquals(fim, response.fim());
        assertEquals(5L, response.duracaoEmMinutos());
        assertNull(response.resultado());
        assertTrue(response.aberta());
    }

    @Test
    void deveConverterSessaoEncerradaComResultadoParaResponse() {

        UUID sessaoId = UUID.randomUUID();

        LocalDateTime inicio =
                LocalDateTime.now().minusMinutes(10);

        LocalDateTime fim =
                LocalDateTime.now().minusMinutes(5);

        Sessao sessao = Sessao.builder()
                .id(sessaoId)
                .inicio(inicio)
                .fim(fim)
                .duracaoEmMinutos(5)
                .resultado(ResultadoVotacao.APROVADA)
                .build();

        SessaoResponse response =
                SessaoMapper.toResponse(
                        sessao,
                        ResultadoVotacao.APROVADA
                );

        assertNotNull(response);
        assertEquals(sessaoId, response.id());
        assertEquals(inicio, response.inicio());
        assertEquals(fim, response.fim());
        assertEquals(5L, response.duracaoEmMinutos());
        assertEquals(
                ResultadoVotacao.APROVADA,
                response.resultado()
        );
        assertFalse(response.aberta());
    }

    @Test
    void deveCalcularDuracaoPeloInicioEFim() {

        LocalDateTime inicio =
                LocalDateTime.of(
                        2026,
                        5,
                        17,
                        10,
                        0
                );

        LocalDateTime fim =
                LocalDateTime.of(
                        2026,
                        5,
                        17,
                        10,
                        15
                );

        Sessao sessao = Sessao.builder()
                .id(UUID.randomUUID())
                .inicio(inicio)
                .fim(fim)
                .duracaoEmMinutos(99)
                .build();

        SessaoResponse response =
                SessaoMapper.toResponse(
                        sessao,
                        null
                );

        assertEquals(
                15L,
                response.duracaoEmMinutos()
        );
    }
}