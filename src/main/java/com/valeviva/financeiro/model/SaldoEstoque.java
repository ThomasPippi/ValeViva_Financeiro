
package com.valeviva.financeiro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "saldo_estoque",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"produto_id", "lote_id"}
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaldoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "lote_id", nullable = false)
    private Long loteId;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal quantidade = BigDecimal.ZERO;

    @Version
    private Long version;

    public void entrada(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade de entrada deve ser maior que zero."
            );
        }

        quantidade = quantidade.add(valor);
    }

    public void baixa(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade de saída deve ser maior que zero."
            );
        }

        if (quantidade.compareTo(valor) < 0) {
            throw new IllegalArgumentException(
                    "Estoque insuficiente."
            );
        }

        quantidade = quantidade.subtract(valor);
    }
}
