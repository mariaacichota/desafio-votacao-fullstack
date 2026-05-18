package com.cooperativa.votacao.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "associados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Associado {

    @Id
    private UUID id;

    private Integer associadoCodigo;

    private String nome;

    private String endereco;

    private String cpf;

    private LocalDateTime associadoEm;

    @ManyToMany
    @JoinTable(
            name = "associado_assuntos",
            joinColumns = @JoinColumn(name = "associado_id"),
            inverseJoinColumns = @JoinColumn(name = "assunto_id")
    )
    private Set<Assunto> assuntos;
}