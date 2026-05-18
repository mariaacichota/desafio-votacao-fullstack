package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.entity.Associado;
import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.domain.entity.Voto;
import com.cooperativa.votacao.domain.enums.OpcaoVoto;
import com.cooperativa.votacao.domain.repository.AssociadoRepository;
import com.cooperativa.votacao.domain.repository.SessaoRepository;
import com.cooperativa.votacao.domain.repository.VotoRepository;
import com.cooperativa.votacao.dto.request.VotoRequest;
import com.cooperativa.votacao.exception.NotFoundException;
import com.cooperativa.votacao.validator.VotoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private VotoValidator validator;

    @InjectMocks
    private VotoService service;

    @Test
    void deveRegistrarVotoComSucesso() {

        UUID sessaoId = UUID.randomUUID();
        UUID associadoId = UUID.randomUUID();

        Sessao sessao = Sessao.builder()
                .id(sessaoId)
                .build();

        Associado associado = Associado.builder()
                .id(associadoId)
                .nome("Maria")
                .build();

        VotoRequest request = new VotoRequest(
                associadoId,
                OpcaoVoto.SIM
        );

        when(sessaoRepository.findById(sessaoId))
                .thenReturn(Optional.of(sessao));

        when(associadoRepository.findById(associadoId))
                .thenReturn(Optional.of(associado));

        service.votar(sessaoId, request);

        ArgumentCaptor<Voto> captor =
                ArgumentCaptor.forClass(Voto.class);

        verify(validator).validar(sessao, associado);
        verify(votoRepository).save(captor.capture());

        Voto votoSalvo = captor.getValue();

        assertEquals(sessao, votoSalvo.getSessao());
        assertEquals(associado, votoSalvo.getAssociado());
        assertEquals(OpcaoVoto.SIM, votoSalvo.getOpcao());
        assertNotNull(votoSalvo.getVotadoEm());
    }

    @Test
    void deveLancarExcecaoQuandoSessaoNaoExistir() {

        UUID sessaoId = UUID.randomUUID();
        UUID associadoId = UUID.randomUUID();

        VotoRequest request = new VotoRequest(
                associadoId,
                OpcaoVoto.SIM
        );

        when(sessaoRepository.findById(sessaoId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.votar(sessaoId, request)
        );

        assertEquals(
                "Sessão não encontrada",
                exception.getMessage()
        );

        verify(associadoRepository, never()).findById(any());
        verify(validator, never()).validar(any(), any());
        verify(votoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoNaoExistir() {

        UUID sessaoId = UUID.randomUUID();
        UUID associadoId = UUID.randomUUID();

        Sessao sessao = Sessao.builder()
                .id(sessaoId)
                .build();

        VotoRequest request = new VotoRequest(
                associadoId,
                OpcaoVoto.SIM
        );

        when(sessaoRepository.findById(sessaoId))
                .thenReturn(Optional.of(sessao));

        when(associadoRepository.findById(associadoId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.votar(sessaoId, request)
        );

        assertEquals(
                "Associado não encontrado",
                exception.getMessage()
        );

        verify(validator, never()).validar(any(), any());
        verify(votoRepository, never()).save(any());
    }

    @Test
    void deveListarVotosPorSessao() {

        UUID sessaoId = UUID.randomUUID();

        Voto voto = Voto.builder()
                .id(UUID.randomUUID())
                .opcao(OpcaoVoto.SIM)
                .build();

        when(votoRepository.findAllBySessaoId(sessaoId))
                .thenReturn(List.of(voto));

        List<Voto> votos =
                service.listarVotos(sessaoId);

        assertEquals(1, votos.size());
        assertEquals(OpcaoVoto.SIM, votos.get(0).getOpcao());

        verify(votoRepository).findAllBySessaoId(sessaoId);
    }
}