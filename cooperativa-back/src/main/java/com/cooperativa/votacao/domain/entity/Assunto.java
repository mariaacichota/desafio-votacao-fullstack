package com.cooperativa.votacao.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "assuntos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assunto {

    @Id
    private UUID id;

    private Integer assuntoCodigo;

    @Column(nullable = false, unique = true)
    private String nome;
}