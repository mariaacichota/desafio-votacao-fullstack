package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.entity.Associado;
import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.domain.repository.AssociadoRepository;
import com.cooperativa.votacao.domain.repository.AssuntoRepository;
import com.cooperativa.votacao.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssociadoService {

    private final AssociadoRepository associadoRepository;
    private final AssuntoRepository assuntoRepository;

    public Associado criar(Associado associado) {

        associado.setAssociadoEm(LocalDateTime.now());

        return associadoRepository.save(associado);
    }

    public void vincularAssunto(UUID associadoId, UUID assuntoId) {

        Associado associado = associadoRepository
                .findById(associadoId)
                .orElseThrow();

        Assunto assunto = assuntoRepository
                .findById(assuntoId)
                .orElseThrow();

        associado.getAssuntos().add(assunto);

        associadoRepository.save(associado);
    }

    public List<Associado> listar() {
        return associadoRepository.findAll();
    }

    public Associado buscarPorId(UUID associadoId) {
        return associadoRepository.findById(associadoId)
                .orElseThrow(() ->
                        new NotFoundException("Associado não encontrado")
                );
    }
}