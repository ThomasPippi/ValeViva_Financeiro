package com.valeviva.financeiro.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "item_necessidade")
@Getter
@Setter
@NoArgsConstructor
public class ItemNecessidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ordem_producao_id", nullable = false)
    private OrdemProducao ordem;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Produto material;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal necessario;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal disponivel;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal falta;
}