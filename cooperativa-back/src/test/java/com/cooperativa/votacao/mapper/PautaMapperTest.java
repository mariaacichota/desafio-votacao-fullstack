package com.cooperativa.votacao.mapper;

import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.domain.entity.Pauta;
import com.cooperativa.votacao.domain.enums.ResultadoVotacao;
import com.cooperativa.votacao.dto.request.PautaRequest;
import com.cooperativa.votacao.dto.response.PautaDetalhadaResponse;
import com.cooperativa.votacao.dto.response.PautaResponse;
import com.cooperativa.votacao.dto.response.SessaoResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PautaMapperTest {

    @Test
    void deveConverterRequestParaEntity() {

        UUID assuntoId = UUID.randomUUID();

        Assunto assunto = Assunto.builder()
                .id(assuntoId)
                .nome("Tecnologia")
                .build();

        PautaRequest request = new PautaRequest(
                "Nova pauta",
                "Descrição da pauta",
                assuntoId
        );

        Pauta pauta = PautaMapper.toEntity(
                request,
                assunto
        );

        assertNotNull(pauta);
        assertNotNull(pauta.getId());
        assertEquals("Nova pauta", pauta.getTitulo());
        assertEquals("Descrição da pauta", pauta.getDescricao());
        assertEquals(assunto, pauta.getAssunto());
        assertNotNull(pauta.getCriadaEm());
    }

    @Test
    void deveConverterEntityParaResponse() {

        UUID pautaId = UUID.randomUUID();

        Pauta pauta = Pauta.builder()
                .id(pautaId)
                .titulo("Pauta teste")
                .descricao("Descrição teste")
                .criadaEm(LocalDateTime.now())
                .build();

        PautaResponse response =
                PautaMapper.toResponse(pauta);

        assertNotNull(response);
        assertEquals(pautaId, response.id());
        assertEquals("Pauta teste", response.titulo());
        assertEquals("Descrição teste", response.descricao());
        assertEquals(pauta.getCriadaEm(), response.createdAt());
    }

    @Test
    void deveConverterEntityParaDetalhadaResponse() {

        UUID pautaId = UUID.randomUUID();

        Pauta pauta = Pauta.builder()
                .id(pautaId)
                .titulo("Pauta detalhada")
                .descricao("Descrição detalhada")
                .criadaEm(LocalDateTime.now())
                .build();

        SessaoResponse sessaoResponse = new SessaoResponse(
                UUID.randomUUID(),
                LocalDateTime.now().minusMinutes(10),
                LocalDateTime.now().minusMinutes(5),
                5L,
                ResultadoVotacao.APROVADA,
                false
        );

        PautaDetalhadaResponse response =
                PautaMapper.toDetalhadaResponse(
                        pauta,
                        List.of(sessaoResponse)
                );

        assertNotNull(response);
        assertEquals(pautaId, response.id());
        assertEquals("Pauta detalhada", response.titulo());
        assertEquals("Descrição detalhada", response.descricao());
        assertEquals(pauta.getCriadaEm(), response.criadaEm());
        assertEquals(1, response.sessoes().size());
        assertEquals(
                ResultadoVotacao.APROVADA,
                response.sessoes().get(0).resultado()
        );
    }
}