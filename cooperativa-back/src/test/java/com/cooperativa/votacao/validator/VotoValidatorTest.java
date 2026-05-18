package com.cooperativa.votacao.validator;

import com.cooperativa.votacao.domain.entity.Associado;
import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.domain.entity.Pauta;
import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.domain.repository.VotoRepository;
import com.cooperativa.votacao.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoValidatorTest {

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private VotoValidator validator;

    @Test
    void deveValidarVotoComSucesso() {

        UUID sessaoId = UUID.randomUUID();
        UUID associadoId = UUID.randomUUID();
        UUID assuntoId = UUID.randomUUID();

        Assunto assunto = Assunto.builder()
                .id(assuntoId)
                .nome("Tecnologia")
                .build();

        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .titulo("Pauta teste")
                .assunto(assunto)
                .build();

        Sessao sessao = Sessao.builder()
                .id(sessaoId)
                .pauta(pauta)
                .inicio(LocalDateTime.now().minusMinutes(1))
                .fim(LocalDateTime.now().plusMinutes(5))
                .build();

        Associado associado = Associado.builder()
                .id(associadoId)
                .nome("Maria")
                .assuntos(new HashSet<>(Set.of(assunto)))
                .build();

        when(votoRepository.existsBySessaoIdAndAssociadoId(
                sessaoId,
                associadoId
        )).thenReturn(false);

        assertDoesNotThrow(() ->
                validator.validar(sessao, associado)
        );

        verify(votoRepository)
                .existsBySessaoIdAndAssociadoId(
                        sessaoId,
                        associadoId
                );
    }

    @Test
    void deveLancarExcecaoQuandoSessaoEstiverEncerrada() {

        Assunto assunto = Assunto.builder()
                .id(UUID.randomUUID())
                .nome("Tecnologia")
                .build();

        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .assunto(assunto)
                .build();

        Sessao sessao = Sessao.builder()
                .id(UUID.randomUUID())
                .pauta(pauta)
                .inicio(LocalDateTime.now().minusMinutes(10))
                .fim(LocalDateTime.now().minusMinutes(1))
                .build();

        Associado associado = Associado.builder()
                .id(UUID.randomUUID())
                .assuntos(new HashSet<>(Set.of(assunto)))
                .build();

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> validator.validar(sessao, associado)
        );

        assertEquals(
                "Sessão encerrada",
                exception.getMessage()
        );

        verify(votoRepository, never())
                .existsBySessaoIdAndAssociadoId(any(), any());
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoNaoPertencerAoAssuntoDaPauta() {

        UUID sessaoId = UUID.randomUUID();

        Assunto assuntoDaPauta = Assunto.builder()
                .id(UUID.randomUUID())
                .nome("Tecnologia")
                .build();

        Assunto assuntoDoAssociado = Assunto.builder()
                .id(UUID.randomUUID())
                .nome("Financeiro")
                .build();

        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .titulo("Pauta tecnologia")
                .assunto(assuntoDaPauta)
                .build();

        Sessao sessao = Sessao.builder()
                .id(sessaoId)
                .pauta(pauta)
                .inicio(LocalDateTime.now().minusMinutes(1))
                .fim(LocalDateTime.now().plusMinutes(5))
                .build();

        Associado associado = Associado.builder()
                .id(UUID.randomUUID())
                .nome("João")
                .assuntos(new HashSet<>(Set.of(assuntoDoAssociado)))
                .build();

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> validator.validar(sessao, associado)
        );

        assertEquals(
                "Associado não pode votar nessa pauta",
                exception.getMessage()
        );

        verify(votoRepository, never())
                .existsBySessaoIdAndAssociadoId(any(), any());
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoJaVotouNaSessao() {

        UUID sessaoId = UUID.randomUUID();
        UUID associadoId = UUID.randomUUID();
        UUID assuntoId = UUID.randomUUID();

        Assunto assunto = Assunto.builder()
                .id(assuntoId)
                .nome("Tecnologia")
                .build();

        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .titulo("Pauta teste")
                .assunto(assunto)
                .build();

        Sessao sessao = Sessao.builder()
                .id(sessaoId)
                .pauta(pauta)
                .inicio(LocalDateTime.now().minusMinutes(1))
                .fim(LocalDateTime.now().plusMinutes(5))
                .build();

        Associado associado = Associado.builder()
                .id(associadoId)
                .nome("Maria")
                .assuntos(new HashSet<>(Set.of(assunto)))
                .build();

        when(votoRepository.existsBySessaoIdAndAssociadoId(
                sessaoId,
                associadoId
        )).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> validator.validar(sessao, associado)
        );

        assertEquals(
                "Associado já votou nessa pauta",
                exception.getMessage()
        );

        verify(votoRepository)
                .existsBySessaoIdAndAssociadoId(
                        sessaoId,
                        associadoId
                );
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoNaoPossuirAssuntos() {

        Assunto assunto = Assunto.builder()
                .id(UUID.randomUUID())
                .nome("Tecnologia")
                .build();

        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .assunto(assunto)
                .build();

        Sessao sessao = Sessao.builder()
                .id(UUID.randomUUID())
                .pauta(pauta)
                .inicio(LocalDateTime.now().minusMinutes(1))
                .fim(LocalDateTime.now().plusMinutes(5))
                .build();

        Associado associado = Associado.builder()
                .id(UUID.randomUUID())
                .nome("Maria")
                .assuntos(new HashSet<>())
                .build();

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> validator.validar(sessao, associado)
        );

        assertEquals(
                "Associado não pode votar nessa pauta",
                exception.getMessage()
        );

        verify(votoRepository, never())
                .existsBySessaoIdAndAssociadoId(any(), any());
    }
}