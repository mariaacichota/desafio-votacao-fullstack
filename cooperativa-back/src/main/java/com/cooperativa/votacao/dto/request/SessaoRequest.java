package com.cooperativa.votacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SessaoRequest(

        @Schema(example = "Duração em minutos da pauta")
        Long duracaoMinutos

) {
}