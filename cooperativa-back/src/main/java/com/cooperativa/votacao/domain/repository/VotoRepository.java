package com.cooperativa.votacao.domain.repository;

import com.cooperativa.votacao.domain.entity.Voto;
import com.cooperativa.votacao.domain.enums.OpcaoVoto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface VotoRepository
        extends JpaRepository<Voto, UUID> {

    boolean existsBySessaoIdAndAssociadoId(UUID sessaoId, UUID associadoId);
    long countBySessaoIdAndOpcao(UUID sessaoId, OpcaoVoto opcao);
    List<Voto> findAllBySessaoId(UUID sessaoId);

}