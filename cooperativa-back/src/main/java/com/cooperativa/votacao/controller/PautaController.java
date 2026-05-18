package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.dto.request.PautaRequest;
import com.cooperativa.votacao.dto.response.PautaDetalhadaResponse;
import com.cooperativa.votacao.dto.response.PautaResponse;
import com.cooperativa.votacao.service.PautaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService service;

    @PostMapping
    public ResponseEntity<PautaResponse> criar(@RequestBody @Valid PautaRequest request) {

        PautaResponse response = service.criar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public List<PautaDetalhadaResponse> listarPautas() {
        return service.buscarTodasPautas();
    }

    @GetMapping("/{pautaId}")
    public ResponseEntity<PautaDetalhadaResponse> buscar(@PathVariable UUID pautaId) {
        return ResponseEntity.ok(
                service.buscarDetalhada(pautaId)
        );
    }
}