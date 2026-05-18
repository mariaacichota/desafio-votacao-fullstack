package com.cooperativa.votacao.validator;

import com.cooperativa.votacao.domain.entity.Associado;
import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.domain.repository.VotoRepository;
import com.cooperativa.votacao.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class VotoValidator {

    private final VotoRepository votoRepository;

    public void validar(
            Sessao sessao,
            Associado associado
    ) {
        validarSessaoAberta(sessao);
        validarAssociadoDoAssunto(sessao, associado);
        validarVotoDuplicado(sessao, associado);
    }

    private void validarSessaoAberta(Sessao sessao) {

        if (sessao.getFim().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Sessão encerrada");
        }
    }

    private void validarAssociadoDoAssunto(
            Sessao sessao,
            Associado associado
    ) {
        boolean associadoPodeVotar =
                associado.getAssuntos()
                        .stream()
                        .anyMatch(assunto ->
                                assunto.getId().equals(
                                        sessao.getPauta()
                                                .getAssunto()
                                                .getId()
                                )
                        );

        if (!associadoPodeVotar) {
            throw new BusinessException(
                    "Associado não pode votar nessa pauta"
            );
        }
    }

    private void validarVotoDuplicado(
            Sessao sessao,
            Associado associado
    ) {
        boolean jaVotou =
                votoRepository.existsBySessaoIdAndAssociadoId(
                        sessao.getId(),
                        associado.getId()
                );

        if (jaVotou) {
            throw new BusinessException(
                    "Associado já votou nessa pauta"
            );
        }
    }
}