package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.domain.enums.OpcaoVoto;
import com.cooperativa.votacao.domain.enums.ResultadoVotacao;
import com.cooperativa.votacao.domain.repository.SessaoRepository;
import com.cooperativa.votacao.domain.repository.VotoRepository;
import com.cooperativa.votacao.dto.response.ResultadoResponse;
import com.cooperativa.votacao.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultadoServiceTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private ResultadoService service;

    @Test
    void deveRetornarResultadoAprovada() {

        UUID sessaoId = UUID.randomUUID();

        Sessao sessao = Sessao.builder()
                .id(sessaoId)
                .fim(LocalDateTime.now().minusMinutes(1))
                .build();

        when(sessaoRepository.findById(sessaoId))
                .thenReturn(Optional.of(sessao));

        when(votoRepository.countBySessaoIdAndOpcao(
                sessaoId,
                OpcaoVoto.SIM
        )).thenReturn(5L);

        when(votoRepository.countBySessaoIdAndOpcao(
                sessaoId,
                OpcaoVoto.NAO
        )).thenReturn(2L);

        ResultadoResponse response =
                service.calcularResultado(sessaoId);

        assertEquals(sessaoId, response.sessaoId());
        assertEquals(5L, response.votosSim());
        assertEquals(2L, response.votosNao());
        assertEquals(
                ResultadoVotacao.APROVADA,
                response.resultado()
        );
    }

    @Test
    void deveRetornarResultadoReprovada() {

        UUID sessaoId = UUID.randomUUID();

        Sessao sessao = Sessao.builder()
                .id(sessaoId)
                .fim(LocalDateTime.now().minusMinutes(1))
                .build();

        when(sessaoRepository.findById(sessaoId))
                .thenReturn(Optional.of(sessao));

        when(votoRepository.countBySessaoIdAndOpcao(
                sessaoId,
                OpcaoVoto.SIM
        )).thenReturn(1L);

        when(votoRepository.countBySessaoIdAndOpcao(
                sessaoId,
                OpcaoVoto.NAO
        )).thenReturn(4L);

        ResultadoResponse response =
                service.calcularResultado(sessaoId);

        assertEquals(
                ResultadoVotacao.REPROVADA,
                response.resultado()
        );
    }

    @Test
    void deveRetornarResultadoEmpate() {

        UUID sessaoId = UUID.randomUUID();

        Sessao sessao = Sessao.builder()
                .id(sessaoId)
                .fim(LocalDateTime.now().minusMinutes(1))
                .build();

        when(sessaoRepository.findById(sessaoId))
                .thenReturn(Optional.of(sessao));

        when(votoRepository.countBySessaoIdAndOpcao(
                sessaoId,
                OpcaoVoto.SIM
        )).thenReturn(3L);

        when(votoRepository.countBySessaoIdAndOpcao(
                sessaoId,
                OpcaoVoto.NAO
        )).thenReturn(3L);

        ResultadoResponse response =
                service.calcularResultado(sessaoId);

        assertEquals(
                ResultadoVotacao.EMPATE,
                response.resultado()
        );
    }

    @Test
    void deveLancarExcecaoQuandoSessaoNaoExistir() {

        UUID sessaoId = UUID.randomUUID();

        when(sessaoRepository.findById(sessaoId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.calcularResultado(sessaoId)
        );

        assertEquals(
                "Sessão não encontrada",
                exception.getMessage()
        );

        verify(votoRepository, never())
                .countBySessaoIdAndOpcao(any(), any());
    }
}