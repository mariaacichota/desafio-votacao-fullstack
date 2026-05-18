package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.domain.entity.Pauta;
import com.cooperativa.votacao.domain.repository.AssuntoRepository;
import com.cooperativa.votacao.domain.repository.PautaRepository;
import com.cooperativa.votacao.domain.repository.SessaoRepository;
import com.cooperativa.votacao.dto.request.PautaRequest;
import com.cooperativa.votacao.dto.response.PautaResponse;
import com.cooperativa.votacao.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private ResultadoService resultadoService;

    @Mock
    private AssuntoRepository assuntoRepository;

    @InjectMocks
    private PautaService service;

    @Test
    void deveCriarPautaComAssunto() {

        UUID assuntoId = UUID.randomUUID();

        PautaRequest request = new PautaRequest(
                "Nova pauta",
                "Descrição da pauta",
                assuntoId
        );

        Assunto assunto = Assunto.builder()
                .id(assuntoId)
                .nome("Tecnologia")
                .build();

        when(assuntoRepository.findById(assuntoId))
                .thenReturn(Optional.of(assunto));

        when(pautaRepository.save(any(Pauta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PautaResponse response = service.criar(request);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("Nova pauta", response.titulo());
        assertEquals("Descrição da pauta", response.descricao());

        verify(assuntoRepository).findById(assuntoId);
        verify(pautaRepository).save(any(Pauta.class));
    }

    @Test
    void deveLancarExcecaoQuandoAssuntoNaoExistirAoCriarPauta() {

        UUID assuntoId = UUID.randomUUID();

        PautaRequest request = new PautaRequest(
                "Nova pauta",
                "Descrição da pauta",
                assuntoId
        );

        when(assuntoRepository.findById(assuntoId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.criar(request)
        );

        assertEquals(
                "Assunto não encontrado",
                exception.getMessage()
        );

        verify(assuntoRepository).findById(assuntoId);
        verify(pautaRepository, never()).save(any());
    }

    @Test
    void deveBuscarPautaPorId() {

        UUID pautaId = UUID.randomUUID();

        Pauta pauta = Pauta.builder()
                .id(pautaId)
                .titulo("Pauta teste")
                .descricao("Descrição teste")
                .build();

        when(pautaRepository.findById(pautaId))
                .thenReturn(Optional.of(pauta));

        PautaResponse response = service.buscarPorId(pautaId);

        assertNotNull(response);
        assertEquals(pautaId, response.id());
        assertEquals("Pauta teste", response.titulo());
        assertEquals("Descrição teste", response.descricao());

        verify(pautaRepository).findById(pautaId);
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoExistirAoBuscarPorId() {

        UUID pautaId = UUID.randomUUID();

        when(pautaRepository.findById(pautaId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.buscarPorId(pautaId)
        );

        assertEquals(
                "Pauta não encontrada",
                exception.getMessage()
        );

        verify(pautaRepository).findById(pautaId);
    }
}