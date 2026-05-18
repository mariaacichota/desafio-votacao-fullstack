package com.cooperativa.votacao.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record PautaResponse(

        UUID id,
        String titulo,
        String descricao,
        LocalDateTime createdAt

) {
}