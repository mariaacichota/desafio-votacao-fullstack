package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.entity.Associado;
import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.domain.entity.Voto;
import com.cooperativa.votacao.domain.repository.AssociadoRepository;
import com.cooperativa.votacao.domain.repository.SessaoRepository;
import com.cooperativa.votacao.domain.repository.VotoRepository;
import com.cooperativa.votacao.dto.request.VotoRequest;
import com.cooperativa.votacao.exception.NotFoundException;
import com.cooperativa.votacao.validator.VotoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoRepository sessaoRepository;
    private final AssociadoRepository associadoRepository;
    private final VotoValidator validator;

    @Transactional
    public void votar(
            UUID sessaoId,
            VotoRequest request
    ) {
        Sessao sessao =
                sessaoRepository.findById(sessaoId)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Sessão não encontrada"
                                )
                        );

        Associado associado =
                associadoRepository.findById(request.associadoId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Associado não encontrado"
                                )
                        );

        validator.validar(
                sessao,
                associado
        );

        Voto voto =
                Voto.builder()
                        .sessao(sessao)
                        .associado(associado)
                        .opcao(request.opcao())
                        .votadoEm(LocalDateTime.now())
                        .build();

        votoRepository.save(voto);
    }

    public List<Voto> listarVotos(UUID sessaoId) {
        return votoRepository.findAllBySessaoId(sessaoId);
    }
}