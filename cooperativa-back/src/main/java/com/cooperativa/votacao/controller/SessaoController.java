package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.dto.request.SessaoRequest;
import com.cooperativa.votacao.dto.response.SessaoResponse;
import com.cooperativa.votacao.service.SessaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pautas/{pautaId}/sessoes")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoService service;

    @PostMapping
    public ResponseEntity<SessaoResponse> abrir(
            @PathVariable UUID pautaId,
            @RequestBody(required = false) SessaoRequest request
    ) {
        return ResponseEntity.ok(service.abrirSessao(pautaId, request));
    }

    @GetMapping
    public ResponseEntity<List<SessaoResponse>> listar(@PathVariable UUID pautaId) {
        return ResponseEntity.ok(service.listarPorPauta(pautaId));
    }

    @GetMapping("/{sessaoId}")
    public ResponseEntity<SessaoResponse> buscar(
            @PathVariable UUID pautaId,
            @PathVariable UUID sessaoId
    ) {
        return ResponseEntity.ok(service.buscar(pautaId, sessaoId));
    }
}