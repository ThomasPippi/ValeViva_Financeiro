package com.valeviva.financeiro.model;

<<<<<<< HEAD
import java.math.BigDecimal;

public class ItemPedidoCompra {
    private Long id;
    private PedidoCompra pedido;
    private Produto produto;
    private BigDecimal quantidade;
    private double preco_unitario;
=======
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
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
    private BigDecimal quantidade_recebida;
}
