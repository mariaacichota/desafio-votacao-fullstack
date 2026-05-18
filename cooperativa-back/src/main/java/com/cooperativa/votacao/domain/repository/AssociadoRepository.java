package com.cooperativa.votacao.domain.repository;

import com.cooperativa.votacao.domain.entity.Associado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssociadoRepository
        extends JpaRepository<Associado, UUID> {
}