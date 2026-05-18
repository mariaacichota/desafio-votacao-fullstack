package com.cooperativa.votacao.domain.repository;

import com.cooperativa.votacao.domain.entity.Sessao;
import com.cooperativa.votacao.dto.response.SessaoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessaoRepository
        extends JpaRepository<Sessao, UUID> {

    List<Sessao> findAllByPautaId(UUID pautaId);
    Optional<Sessao> findByIdAndPautaId(UUID sessaoId, UUID pautaId);
    Optional<Sessao> findFirstByPautaIdOrderByFimDesc(UUID pautaId);
    boolean existsByPautaIdAndFimAfter(UUID pautaId, LocalDateTime agora);
}