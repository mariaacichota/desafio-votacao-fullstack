package com.cooperativa.votacao.domain.repository;

import com.cooperativa.votacao.domain.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PautaRepository
        extends JpaRepository<Pauta, UUID> {
}