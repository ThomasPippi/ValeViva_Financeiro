
package com.valeviva.financeiro.model;

import com.valeviva.financeiro.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_estoque")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoMovimentacao tipo;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "lote_id", nullable = false)
    private Long loteId;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal quantidade;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "origem_destino", length = 150)
    private String origemDestino;

    @Column(name = "expedicao_id")
    private Long expedicaoId;

    @PrePersist
    public void registrarData() {
        if (dataHora == null) {
            dataHora = LocalDateTime.now();
        }
    }
}
