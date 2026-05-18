package com.cooperativa.votacao.dto.response;

import com.cooperativa.votacao.domain.enums.ResultadoVotacao;

import java.util.UUID;

public record ResultadoResponse(

        UUID sessaoId,
        Long votosSim,
        Long votosNao,
        ResultadoVotacao resultado

) {
}