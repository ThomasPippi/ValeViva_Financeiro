package com.valeviva.financeiro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "ItemPedidoCompra")
@Table(name = "item_pedido_compra")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemPedidoCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_compra_id", nullable = false)
    private PedidoCompra pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(precision = 15, scale = 4)
    private BigDecimal quantidade;

    @Column(precision = 12, scale = 2)
    private BigDecimal preco_unitario;

    @Column(precision = 15, scale = 4)
    private BigDecimal quantidade_recebida;
}
