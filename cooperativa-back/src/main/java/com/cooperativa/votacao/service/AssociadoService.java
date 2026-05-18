package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.entity.Associado;
import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.domain.repository.AssociadoRepository;
import com.cooperativa.votacao.domain.repository.AssuntoRepository;
import com.cooperativa.votacao.exception.NotFoundException;
import com.cooperativa.votacao.validator.CpfValidator;
import jakarta.transaction.Transactional;
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
    private final CpfValidator cpfValidator;

    @Transactional
    public Associado criar(Associado associado) {

        cpfValidator.validar(associado.getCpf());

        if (associado.getId() == null) {
            associado.setId(UUID.randomUUID());
        }

        associado.setCpf(
                associado.getCpf().replaceAll("\\D", "")
        );

        associado.setAssociadoEm(LocalDateTime.now());

        return associadoRepository.save(associado);
    }

    public void vincularAssunto(UUID associadoId, UUID assuntoId) {

        Associado associado = associadoRepository
                .findById(associadoId)
                .orElseThrow(() ->
                        new NotFoundException("Associado não encontrado")
                );

        Assunto assunto = assuntoRepository
                .findById(assuntoId)
                .orElseThrow(() ->
                        new NotFoundException("Assunto não encontrado")
                );

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