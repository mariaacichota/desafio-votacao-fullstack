package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.domain.entity.Voto;
import com.cooperativa.votacao.dto.request.VotoRequest;
import com.cooperativa.votacao.service.VotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sessoes/{sessaoId}/votos")
@RequiredArgsConstructor
public class VotoController {

    private final VotoService service;

    @PostMapping
    public ResponseEntity<Void> votar(
            @PathVariable UUID sessaoId,
            @RequestBody @Valid VotoRequest request
    ) {
        service.votar(sessaoId, request);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Voto>> listar(@PathVariable UUID sessaoId) {
        return ResponseEntity.ok(service.listarVotos(sessaoId));
    }
}