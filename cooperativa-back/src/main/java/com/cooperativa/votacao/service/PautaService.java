package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.domain.entity.Pauta;
import com.cooperativa.votacao.domain.enums.ResultadoVotacao;
import com.cooperativa.votacao.domain.repository.AssuntoRepository;
import com.cooperativa.votacao.domain.repository.PautaRepository;
import com.cooperativa.votacao.domain.repository.SessaoRepository;
import com.cooperativa.votacao.dto.request.PautaRequest;
import com.cooperativa.votacao.dto.response.PautaDetalhadaResponse;
import com.cooperativa.votacao.dto.response.PautaResponse;
import com.cooperativa.votacao.dto.response.SessaoResponse;
import com.cooperativa.votacao.exception.NotFoundException;
import com.cooperativa.votacao.mapper.PautaMapper;
import com.cooperativa.votacao.mapper.SessaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository repository;
    private final ResultadoService resultadoService;
    private final AssuntoRepository assuntoRepository;
    private final SessaoRepository sessaoRepository;

    @Transactional
    public PautaResponse criar(PautaRequest request) {

        Assunto assunto =
                assuntoRepository.findById(request.assuntoId())
                        .orElseThrow(() ->
                                new NotFoundException("Assunto não encontrado")
                        );

        Pauta pauta =
                PautaMapper.toEntity(request, assunto);

        Pauta saved = repository.save(pauta);

        return PautaMapper.toResponse(saved);
    }

    public PautaResponse buscarPorId(UUID pautaId) {

        Pauta pauta = repository.findById(pautaId)
                .orElseThrow(() ->
                        new NotFoundException("Pauta não encontrada")
                );

        return PautaMapper.toResponse(pauta);
    }

    public List<PautaDetalhadaResponse> buscarTodasPautas() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private PautaDetalhadaResponse toResponse(Pauta pauta) {

        List<SessaoResponse> sessoes = sessaoRepository
                .findAllByPautaId(pauta.getId())
                .stream()
                .map(sessao -> {

                    ResultadoVotacao resultado = null;

                    boolean aberta =
                            sessao.getFim().isAfter(LocalDateTime.now());

                    if (!aberta) {
                        resultado =
                                resultadoService
                                        .calcularResultado(sessao.getId())
                                        .resultado();
                    }

                    return new SessaoResponse(
                            sessao.getId(),
                            sessao.getInicio(),
                            sessao.getFim(),
                            sessao.getDuracaoEmMinutos().longValue(),
                            resultado,
                            aberta
                    );
                })
                .toList();

        return PautaMapper.toDetalhadaResponse(
                pauta,
                sessoes
        );
    }

    public PautaDetalhadaResponse buscarDetalhada(UUID pautaId) {

        Pauta pauta = repository.findById(pautaId)
                .orElseThrow(() ->
                        new NotFoundException("Pauta não encontrada")
                );

        return toResponse(pauta);
    }
}