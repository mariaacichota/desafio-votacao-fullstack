package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.domain.enums.OpcaoVoto;
import com.cooperativa.votacao.domain.enums.ResultadoVotacao;
import com.cooperativa.votacao.dto.response.ResultadoResponse;
import com.cooperativa.votacao.exception.NotFoundException;
import com.cooperativa.votacao.domain.repository.SessaoRepository;
import com.cooperativa.votacao.domain.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResultadoService {

    private final SessaoRepository sessaoRepository;
    private final VotoRepository votoRepository;

    public ResultadoResponse calcularResultado(
            UUID sessaoId
    ) {

        Sessao sessao =
                sessaoRepository.findById(sessaoId)
                        .orElseThrow(() ->
                                new NotFoundException("Sessão não encontrada")
                        );

        long votosSim = votoRepository.countBySessaoIdAndOpcao(sessaoId, OpcaoVoto.SIM);
        long votosNao = votoRepository.countBySessaoIdAndOpcao(sessaoId, OpcaoVoto.NAO);

        ResultadoVotacao resultado;
        if (votosSim > votosNao) {
            resultado = ResultadoVotacao.APROVADA;
        } else if (votosNao > votosSim) {
            resultado = ResultadoVotacao.REPROVADA;
        } else {
            resultado = ResultadoVotacao.EMPATE;
        }

        if (sessao.getFim().isBefore(LocalDateTime.now())) {
            if (sessao.getResultado() == null || !sessao.getResultado().equals(resultado)) {
                sessao.setResultado(resultado);
                sessaoRepository.save(sessao);
            }
        }

        return new ResultadoResponse(
                sessao.getId(),
                votosSim,
                votosNao,
                resultado
        );
    }
}