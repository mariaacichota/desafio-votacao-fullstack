package com.cooperativa.votacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Dados para criação de pauta")
public record PautaRequest(

        @NotBlank
        @Schema(example = "Nova pauta")
        String titulo,

        @Schema(example = "Descrição da pauta")
        String descricao,

        @NotNull
        UUID assuntoId

) {
}