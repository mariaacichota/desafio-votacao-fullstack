package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.service.AssuntoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assuntos")
@RequiredArgsConstructor
public class AssuntoController {

    private final AssuntoService service;

    @PostMapping
    public ResponseEntity<Assunto> criar(@RequestBody Assunto assunto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criar(assunto));
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(service.listar());
    }
}