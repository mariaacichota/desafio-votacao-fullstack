package com.cooperativa.votacao.domain.repository;

import com.cooperativa.votacao.domain.entity.Assunto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssuntoRepository
        extends JpaRepository<Assunto, UUID> {
}