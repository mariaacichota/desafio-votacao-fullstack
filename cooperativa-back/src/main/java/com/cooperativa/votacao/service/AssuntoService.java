package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.domain.repository.AssuntoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssuntoService {

    private final AssuntoRepository repository;

    public Assunto criar(Assunto assunto) {
        return repository.save(assunto);
    }

    public List<Assunto> listar() {
        return repository.findAll();
    }
}