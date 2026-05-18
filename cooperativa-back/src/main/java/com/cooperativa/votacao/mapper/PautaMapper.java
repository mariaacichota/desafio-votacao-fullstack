package com.cooperativa.votacao.mapper;

import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.domain.entity.Pauta;
import com.cooperativa.votacao.dto.request.PautaRequest;
import com.cooperativa.votacao.dto.response.PautaDetalhadaResponse;
import com.cooperativa.votacao.dto.response.PautaResponse;
import com.cooperativa.votacao.dto.response.SessaoResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PautaMapper {

    private PautaMapper() {}

    public static Pauta toEntity(PautaRequest request, Assunto assunto) {

        return Pauta.builder()
                .id(UUID.randomUUID())
                .titulo(request.titulo())
                .descricao(request.descricao())
                .assunto(assunto)
                .criadaEm(LocalDateTime.now())
                .build();
    }

    public static PautaResponse toResponse(Pauta pauta) {

        return new PautaResponse(
                pauta.getId(),
                pauta.getTitulo(),
                pauta.getDescricao(),
                pauta.getCriadaEm()
        );
    }

    public static PautaDetalhadaResponse toDetalhadaResponse(
            Pauta pauta,
            List<SessaoResponse> sessoes
    ) {

        return new PautaDetalhadaResponse(
                pauta.getId(),
                pauta.getTitulo(),
                pauta.getDescricao(),
                pauta.getCriadaEm(),
                sessoes
        );
    }
}