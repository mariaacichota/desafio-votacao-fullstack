package com.cooperativa.votacao.dto.response;

import com.cooperativa.votacao.domain.enums.ResultadoVotacao;

import java.time.LocalDateTime;

import java.util.UUID;

public record SessaoResponse(

        UUID id,
        LocalDateTime inicio,
        LocalDateTime fim,
        Long duracaoEmMinutos,
        ResultadoVotacao resultado,
        Boolean aberta

) {
}