package com.cooperativa.votacao.repository;

import com.cooperativa.votacao.domain.entity.*;
import com.cooperativa.votacao.domain.enums.OpcaoVoto;
import com.cooperativa.votacao.domain.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VotoRepositoryTest {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Test
    void deveVerificarSeAssociadoJaVotouNaSessao() {

        Dados dados = criarDadosBase();

        Voto voto = Voto.builder()
                .id(UUID.randomUUID())
                .sessao(dados.sessao())
                .associado(dados.associado())
                .opcao(OpcaoVoto.SIM)
                .votadoEm(LocalDateTime.now())
                .build();

        votoRepository.save(voto);

        boolean existe =
                votoRepository.existsBySessaoIdAndAssociadoId(
                        dados.sessao().getId(),
                        dados.associado().getId()
                );

        assertTrue(existe);
    }

    @Test
    void deveContarVotosPorSessaoEOpcao() {

        Dados dados = criarDadosBase();

        Associado associado2 = Associado.builder()
                .id(UUID.randomUUID())
                .associadoCodigo(2)
                .nome("João")
                .cpf("12345678902")
                .endereco("Rua 2")
                .associadoEm(LocalDateTime.now())
                .build();

        associadoRepository.save(associado2);

        votoRepository.save(
                Voto.builder()
                        .id(UUID.randomUUID())
                        .sessao(dados.sessao())
                        .associado(dados.associado())
                        .opcao(OpcaoVoto.SIM)
                        .votadoEm(LocalDateTime.now())
                        .build()
        );

        votoRepository.save(
                Voto.builder()
                        .id(UUID.randomUUID())
                        .sessao(dados.sessao())
                        .associado(associado2)
                        .opcao(OpcaoVoto.NAO)
                        .votadoEm(LocalDateTime.now())
                        .build()
        );

        long sim =
                votoRepository.countBySessaoIdAndOpcao(
                        dados.sessao().getId(),
                        OpcaoVoto.SIM
                );

        long nao =
                votoRepository.countBySessaoIdAndOpcao(
                        dados.sessao().getId(),
                        OpcaoVoto.NAO
                );

        assertEquals(1L, sim);
        assertEquals(1L, nao);
    }

    @Test
    void deveListarVotosPorSessao() {

        Dados dados = criarDadosBase();

        Voto voto = Voto.builder()
                .id(UUID.randomUUID())
                .sessao(dados.sessao())
                .associado(dados.associado())
                .opcao(OpcaoVoto.SIM)
                .votadoEm(LocalDateTime.now())
                .build();

        votoRepository.save(voto);

        List<Voto> votos =
                votoRepository.findAllBySessaoId(
                        dados.sessao().getId()
                );

        assertEquals(1, votos.size());
        assertEquals(OpcaoVoto.SIM, votos.get(0).getOpcao());
    }

    private Dados criarDadosBase() {

        Assunto assunto = Assunto.builder()
                .id(UUID.randomUUID())
                .assuntoCodigo((int) (System.currentTimeMillis() % 100000))
                .nome("Tecnologia-" + UUID.randomUUID())
                .build();

        assunto = assuntoRepository.saveAndFlush(assunto);

        Associado associado = Associado.builder()
                .id(UUID.randomUUID())
                .associadoCodigo((int) (System.currentTimeMillis() % 100000))
                .nome("Maria")
                .cpf(String.valueOf(System.nanoTime()).substring(0, 11))
                .endereco("Rua 1")
                .associadoEm(LocalDateTime.now())
                .assuntos(new HashSet<>(Set.of(assunto)))
                .build();

        associado = associadoRepository.saveAndFlush(associado);

        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .titulo("Pauta teste")
                .descricao("Descrição teste")
                .criadaEm(LocalDateTime.now())
                .assunto(assunto)
                .build();

        pauta = pautaRepository.saveAndFlush(pauta);

        Sessao sessao = Sessao.builder()
                .id(UUID.randomUUID())
                .pauta(pauta)
                .inicio(LocalDateTime.now())
                .fim(LocalDateTime.now().plusMinutes(5))
                .duracaoEmMinutos(5)
                .build();

        sessao = sessaoRepository.saveAndFlush(sessao);

        return new Dados(sessao, associado);
    }

    private record Dados(
            Sessao sessao,
            Associado associado
    ) {
    }
}