package com.valeviva.financeiro.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "operacao")
@Getter
@Setter
@NoArgsConstructor
public class Operacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roteiro_id", nullable = false)
    private Roteiro roteiro;

    @Column(nullable = false)
    private Integer sequencia;

    @Column(nullable = false, length = 150)
    private String descricao;

    // Pode ficar vazio (a coluna aceita NULL)
    @ManyToOne
    @JoinColumn(name = "maquina_id")
    private Maquina maquina;

    @Column(name = "tempo_padrao_minutos", nullable = false, precision = 10, scale = 2)
    private BigDecimal tempoPadraoMinutos;
}