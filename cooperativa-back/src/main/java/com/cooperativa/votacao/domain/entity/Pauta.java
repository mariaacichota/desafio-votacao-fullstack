package com.cooperativa.votacao.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pautas")
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String titulo;

    private String descricao;

    private LocalDateTime criadaEm;

    @OneToMany(
            mappedBy = "pauta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Sessao> sessoes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "assunto_id")
    private Assunto assunto;
}