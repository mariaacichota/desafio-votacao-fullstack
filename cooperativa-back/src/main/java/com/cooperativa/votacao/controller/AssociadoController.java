package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.domain.entity.Associado;
import com.cooperativa.votacao.service.AssociadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/associados")
@RequiredArgsConstructor
public class AssociadoController {

    private final AssociadoService service;

    @PostMapping
    public ResponseEntity<Associado> criar(@RequestBody Associado associado) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criar(associado));
    }

    @PostMapping("/{associadoId}/assuntos/{assuntoId}")
    public ResponseEntity<Void> vincularAssunto(
            @PathVariable UUID associadoId,
            @PathVariable UUID assuntoId
    ) {

        service.vincularAssunto(
                associadoId,
                assuntoId
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Associado>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{associadoId}")
    public ResponseEntity<Associado> buscar(
            @PathVariable UUID associadoId
    ) {
        return ResponseEntity.ok(service.buscarPorId(associadoId));
    }
}