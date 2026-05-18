package com.cooperativa.votacao.dto.request;

import com.cooperativa.votacao.domain.enums.OpcaoVoto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VotoRequest(

        @NotNull
        UUID associadoId,

        @NotNull
        @Schema(example = "Opção de voto")
        OpcaoVoto opcao

) {
}