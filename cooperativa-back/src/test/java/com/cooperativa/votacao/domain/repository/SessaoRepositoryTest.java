package com.cooperativa.votacao.repository;

import com.cooperativa.votacao.domain.entity.Assunto;
import com.cooperativa.votacao.domain.entity.Pauta;
import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.domain.repository.AssuntoRepository;
import com.cooperativa.votacao.domain.repository.PautaRepository;
import com.cooperativa.votacao.domain.repository.SessaoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        statements = {
                "TRUNCATE TABLE votos RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE sessoes RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE pautas RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE associado_assuntos RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE associados RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE assuntos RESTART IDENTITY CASCADE"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class SessaoRepositoryTest {

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Test
    void deveBuscarSessoesPorPautaId() {

        Pauta pauta = criarPauta();

        Sessao sessao = Sessao.builder()
                .id(UUID.randomUUID())
                .pauta(pauta)
                .inicio(LocalDateTime.now())
                .fim(LocalDateTime.now().plusMinutes(5))
                .duracaoEmMinutos(5)
                .resultado(null)
                .build();

        sessao = sessaoRepository.saveAndFlush(sessao);

        List<Sessao> sessoes =
                sessaoRepository.findAllByPautaId(pauta.getId());

        assertEquals(1, sessoes.size());
        assertEquals(sessao.getId(), sessoes.get(0).getId());
        assertEquals(pauta.getId(), sessoes.get(0).getPauta().getId());
    }

    @Test
    void deveBuscarSessaoPorIdEPautaId() {

        Pauta pauta = criarPauta();

        Sessao sessao = Sessao.builder()
                .id(UUID.randomUUID())
                .pauta(pauta)
                .inicio(LocalDateTime.now())
                .fim(LocalDateTime.now().plusMinutes(5))
                .duracaoEmMinutos(5)
                .resultado(null)
                .build();

        sessao = sessaoRepository.saveAndFlush(sessao);

        Optional<Sessao> resultado =
                sessaoRepository.findByIdAndPautaId(
                        sessao.getId(),
                        pauta.getId()
                );

        assertTrue(resultado.isPresent());
        assertEquals(sessao.getId(), resultado.get().getId());
    }

    @Test
    void deveRetornarTrueQuandoExistirSessaoAberta() {

        Pauta pauta = criarPauta();

        Sessao sessaoAberta = Sessao.builder()
                .id(UUID.randomUUID())
                .pauta(pauta)
                .inicio(LocalDateTime.now().minusMinutes(1))
                .fim(LocalDateTime.now().plusMinutes(10))
                .duracaoEmMinutos(10)
                .resultado(null)
                .build();

        sessaoRepository.saveAndFlush(sessaoAberta);

        boolean existe =
                sessaoRepository.existsByPautaIdAndFimAfter(
                        pauta.getId(),
                        LocalDateTime.now()
                );

        assertTrue(existe);
    }

    @Test
    void deveRetornarFalseQuandoNaoExistirSessaoAberta() {

        Pauta pauta = criarPauta();

        Sessao sessaoEncerrada = Sessao.builder()
                .id(UUID.randomUUID())
                .pauta(pauta)
                .inicio(LocalDateTime.now().minusMinutes(10))
                .fim(LocalDateTime.now().minusMinutes(1))
                .duracaoEmMinutos(9)
                .resultado(null)
                .build();

        sessaoRepository.saveAndFlush(sessaoEncerrada);

        boolean existe =
                sessaoRepository.existsByPautaIdAndFimAfter(
                        pauta.getId(),
                        LocalDateTime.now()
                );

        assertFalse(existe);
    }

    @Test
    void deveBuscarUltimaSessaoDaPautaPelaDataFim() {

        Pauta pauta = criarPauta();

        LocalDateTime base = LocalDateTime.of(
                2026,
                5,
                18,
                10,
                0
        );

        Sessao primeira = Sessao.builder()
                .id(UUID.randomUUID())
                .pauta(pauta)
                .inicio(base)
                .fim(base.plusMinutes(5))
                .duracaoEmMinutos(5)
                .build();

        Sessao ultima = Sessao.builder()
                .id(UUID.randomUUID())
                .pauta(pauta)
                .inicio(base.plusMinutes(10))
                .fim(base.plusMinutes(20))
                .duracaoEmMinutos(10)
                .build();

        sessaoRepository.saveAndFlush(primeira);
        ultima = sessaoRepository.saveAndFlush(ultima);

        Optional<Sessao> resultado =
                sessaoRepository.findFirstByPautaIdOrderByFimDesc(
                        pauta.getId()
                );

        assertTrue(resultado.isPresent());
        assertEquals(ultima.getId(), resultado.get().getId());
    }

    private Pauta criarPauta() {

        Assunto assunto = Assunto.builder()
                .id(UUID.randomUUID())
                .assuntoCodigo((int) (System.nanoTime() % 100000))
                .nome("Tecnologia-" + UUID.randomUUID())
                .build();

        assunto = assuntoRepository.saveAndFlush(assunto);

        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .titulo("Pauta teste")
                .descricao("Descrição teste")
                .criadaEm(LocalDateTime.now())
                .assunto(assunto)
                .build();

        return pautaRepository.saveAndFlush(pauta);
    }
}