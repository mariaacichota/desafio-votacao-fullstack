package com.cooperativa.votacao.domain.entity;


import com.cooperativa.votacao.domain.enums.ResultadoVotacao;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    private LocalDateTime inicio;

    private LocalDateTime fim;

    private Integer duracaoEmMinutos;

    @Enumerated(EnumType.STRING)
    private ResultadoVotacao resultado;
}