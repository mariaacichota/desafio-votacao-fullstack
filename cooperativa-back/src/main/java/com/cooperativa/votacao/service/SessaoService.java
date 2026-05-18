package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.entity.Pauta;
import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.domain.enums.ResultadoVotacao;
import com.cooperativa.votacao.domain.repository.PautaRepository;
import com.cooperativa.votacao.domain.repository.SessaoRepository;
import com.cooperativa.votacao.dto.request.SessaoRequest;
import com.cooperativa.votacao.dto.response.SessaoResponse;
import com.cooperativa.votacao.exception.BusinessException;
import com.cooperativa.votacao.exception.NotFoundException;
import com.cooperativa.votacao.mapper.SessaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessaoService {

    private final SessaoRepository repository;
    private final PautaRepository pautaRepository;
    private final ResultadoService resultadoService;

    public SessaoResponse abrirSessao(
            UUID pautaId,
            SessaoRequest request
    ) {

        Pauta pauta =
                pautaRepository.findById(pautaId)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Pauta não encontrada"
                                )
                        );

        boolean possuiSessaoAberta =
                repository.existsByPautaIdAndFimAfter(
                        pautaId,
                        LocalDateTime.now()
                );

        if (possuiSessaoAberta) {
            throw new BusinessException(
                    "Já existe uma sessão aberta para esta pauta"
            );
        }

        long minutos =
                request == null ||
                        request.duracaoMinutos() == null
                        ? 1
                        : request.duracaoMinutos();

        LocalDateTime inicio = LocalDateTime.now();

        Sessao sessao =
                Sessao.builder()
                        .pauta(pauta)
                        .inicio(inicio)
                        .fim(inicio.plusMinutes(minutos))
                        .duracaoEmMinutos((int) minutos)
                        .resultado(null)
                        .build();

        Sessao salva = repository.save(sessao);

        return SessaoMapper.toResponse(
                salva,
                null
        );
    }

    public List<SessaoResponse> listarPorPauta(UUID pautaId) {

        if (!pautaRepository.existsById(pautaId)) {
            throw new NotFoundException(
                    "Pauta não encontrada"
            );
        }

        return repository.findAllByPautaId(pautaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SessaoResponse buscar(
            UUID pautaId,
            UUID sessaoId
    ) {

        Sessao sessao =
                repository.findByIdAndPautaId(
                                sessaoId,
                                pautaId
                        )
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Sessão não encontrada"
                                )
                        );

        return toResponse(sessao);
    }

    private SessaoResponse toResponse(Sessao sessao) {
        ResultadoVotacao resultado = null;
        boolean aberta = sessao.getFim().isAfter(LocalDateTime.now());

        if (!aberta && sessao.getResultado() != null) {
            resultado = sessao.getResultado();
        }

        return SessaoMapper.toResponse(sessao, resultado);
    }
}