package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.dto.response.ResultadoResponse;
import com.cooperativa.votacao.service.ResultadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ResultadoController {

    private final ResultadoService service;

    @GetMapping("/sessoes/{sessaoId}/resultado")
    public ResponseEntity<ResultadoResponse> resultado(@PathVariable UUID sessaoId) {
        return ResponseEntity.ok(service.calcularResultado(sessaoId));
    }
}