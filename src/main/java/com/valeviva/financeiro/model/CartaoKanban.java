package com.valeviva.financeiro.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Entidade simulada: representa o cartão da ordem no Kanban
@Entity
@Table(name = "cartao_kanban")
@Getter
@Setter
@NoArgsConstructor
public class CartaoKanban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ordem_producao_id", nullable = false)
    private OrdemProducao ordem;

    @Column(nullable = false, length = 30)
    private String etapa;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    // Novo cartão começa na etapa A_FAZER
    public CartaoKanban(OrdemProducao ordem) {
        this.ordem = ordem;
        this.etapa = "A_FAZER";
        this.criadoEm = LocalDateTime.now();
    }
}