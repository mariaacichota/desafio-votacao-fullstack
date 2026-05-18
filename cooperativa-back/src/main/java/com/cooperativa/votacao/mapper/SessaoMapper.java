package com.cooperativa.votacao.mapper;

import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.domain.enums.ResultadoVotacao;
import com.cooperativa.votacao.dto.response.SessaoResponse;

import java.time.Duration;
import java.time.LocalDateTime;

public class SessaoMapper {

    private SessaoMapper() {}

    public static SessaoResponse toResponse(
            Sessao sessao,
            ResultadoVotacao resultado
    ) {
        boolean aberta = sessao.getFim().isAfter(LocalDateTime.now());

        return new SessaoResponse(
                sessao.getId(),
                sessao.getInicio(),
                sessao.getFim(),
                Duration.between(sessao.getInicio(), sessao.getFim()).toMinutes(),
                resultado,
                aberta
        );
    }
}