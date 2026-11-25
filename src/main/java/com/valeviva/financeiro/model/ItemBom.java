package com.valeviva.financeiro.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_bom")
@Getter
@Setter
@NoArgsConstructor
public class ItemBom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Produto que é fabricado
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // Material que é usado na fabricação
    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Produto material;

    @Column(name = "quantidade_por_unidade", nullable = false, precision = 15, scale = 4)
    private BigDecimal quantidadePorUnidade;

    @Column(nullable = false)
    private Integer versao;

    @Column(nullable = false)
    private boolean ativa;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    // Quantidade de material necessária para produzir a quantidade informada
    public BigDecimal calcularNecessidade(BigDecimal quantidade) {
        return quantidade.multiply(quantidadePorUnidade);
    }
}