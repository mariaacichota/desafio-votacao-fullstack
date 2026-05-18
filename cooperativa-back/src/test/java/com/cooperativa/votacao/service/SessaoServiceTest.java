package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.entity.Pauta;
import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.domain.repository.PautaRepository;
import com.cooperativa.votacao.domain.repository.SessaoRepository;
import com.cooperativa.votacao.dto.request.SessaoRequest;
import com.cooperativa.votacao.dto.response.SessaoResponse;
import com.cooperativa.votacao.exception.BusinessException;
import com.cooperativa.votacao.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoServiceTest {

    @Mock
    private SessaoRepository repository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private ResultadoService resultadoService;

    @InjectMocks
    private SessaoService service;

    @Test
    void deveAbrirSessaoComDuracaoInformada() {

        UUID pautaId = UUID.randomUUID();

        Pauta pauta = Pauta.builder()
                .id(pautaId)
                .titulo("Pauta teste")
                .build();

        SessaoRequest request = new SessaoRequest(5L);

        when(pautaRepository.findById(pautaId))
                .thenReturn(Optional.of(pauta));

        when(repository.existsByPautaIdAndFimAfter(
                eq(pautaId),
                any(LocalDateTime.class)
        )).thenReturn(false);

        when(repository.save(any(Sessao.class)))
                .thenAnswer(invocation -> {
                    Sessao sessao = invocation.getArgument(0);
                    sessao.setId(UUID.randomUUID());
                    return sessao;
                });

        SessaoResponse response = service.abrirSessao(
                pautaId,
                request
        );

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(5L, response.duracaoEmMinutos());
        assertTrue(response.aberta());

        verify(pautaRepository).findById(pautaId);
        verify(repository).existsByPautaIdAndFimAfter(
                eq(pautaId),
                any(LocalDateTime.class)
        );
        verify(repository).save(any(Sessao.class));
    }

    @Test
    void deveAbrirSessaoComDuracaoDefaultQuandoRequestForNull() {

        UUID pautaId = UUID.randomUUID();

        Pauta pauta = Pauta.builder()
                .id(pautaId)
                .titulo("Pauta teste")
                .build();

        when(pautaRepository.findById(pautaId))
                .thenReturn(Optional.of(pauta));

        when(repository.existsByPautaIdAndFimAfter(
                eq(pautaId),
                any(LocalDateTime.class)
        )).thenReturn(false);

        when(repository.save(any(Sessao.class)))
                .thenAnswer(invocation -> {
                    Sessao sessao = invocation.getArgument(0);
                    sessao.setId(UUID.randomUUID());
                    return sessao;
                });

        SessaoResponse response = service.abrirSessao(
                pautaId,
                null
        );

        assertNotNull(response);
        assertEquals(1L, response.duracaoEmMinutos());
        assertTrue(response.aberta());

        verify(repository).save(any(Sessao.class));
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoExistirAoAbrirSessao() {

        UUID pautaId = UUID.randomUUID();

        when(pautaRepository.findById(pautaId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.abrirSessao(
                        pautaId,
                        new SessaoRequest(5L)
                )
        );

        assertEquals(
                "Pauta não encontrada",
                exception.getMessage()
        );

        verify(repository, never()).save(any());
    }

    @Test
    void deveBloquearAberturaQuandoJaExisteSessaoAberta() {

        UUID pautaId = UUID.randomUUID();

        Pauta pauta = Pauta.builder()
                .id(pautaId)
                .titulo("Pauta teste")
                .build();

        when(pautaRepository.findById(pautaId))
                .thenReturn(Optional.of(pauta));

        when(repository.existsByPautaIdAndFimAfter(
                eq(pautaId),
                any(LocalDateTime.class)
        )).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.abrirSessao(
                        pautaId,
                        new SessaoRequest(5L)
                )
        );

        assertEquals(
                "Já existe uma sessão aberta para esta pauta",
                exception.getMessage()
        );

        verify(repository, never()).save(any());
    }

    @Test
    void deveListarSessoesPorPauta() {

        UUID pautaId = UUID.randomUUID();

        Sessao sessao = Sessao.builder()
                .id(UUID.randomUUID())
                .inicio(LocalDateTime.now())
                .fim(LocalDateTime.now().plusMinutes(5))
                .duracaoEmMinutos(5)
                .build();

        when(pautaRepository.existsById(pautaId))
                .thenReturn(true);

        when(repository.findAllByPautaId(pautaId))
                .thenReturn(List.of(sessao));

        List<SessaoResponse> response =
                service.listarPorPauta(pautaId);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertTrue(response.get(0).aberta());

        verify(pautaRepository).existsById(pautaId);
        verify(repository).findAllByPautaId(pautaId);
    }

    @Test
    void deveBuscarSessaoPorPauta() {

        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();

        Sessao sessao = Sessao.builder()
                .id(sessaoId)
                .inicio(LocalDateTime.now())
                .fim(LocalDateTime.now().plusMinutes(5))
                .duracaoEmMinutos(5)
                .build();

        when(repository.findByIdAndPautaId(sessaoId, pautaId))
                .thenReturn(Optional.of(sessao));

        SessaoResponse response =
                service.buscar(pautaId, sessaoId);

        assertNotNull(response);
        assertEquals(sessaoId, response.id());
        assertTrue(response.aberta());

        verify(repository).findByIdAndPautaId(
                sessaoId,
                pautaId
        );
    }
}