package com.cooperativa.votacao.domain.entity;

import com.cooperativa.votacao.domain.enums.OpcaoVoto;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "votos",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"sessao_id", "associado_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sessao_id", nullable = false)
    private Sessao sessao;

    @ManyToOne
    @JoinColumn(name = "associado_id")
    private Associado associado;

    @Enumerated(EnumType.STRING)
    private OpcaoVoto opcao;

    private LocalDateTime votadoEm;
}