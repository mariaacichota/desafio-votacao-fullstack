package com.cooperativa.votacao.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PautaDetalhadaResponse(

        UUID id,
        String titulo,
        String descricao,
        LocalDateTime criadaEm,
        List<SessaoResponse> sessoes

) {
}