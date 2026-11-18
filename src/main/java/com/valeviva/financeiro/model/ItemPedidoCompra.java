package com.valeviva.financeiro.model;

import java.math.BigDecimal;

public class ItemPedidoCompra {
    private Long id;
    private PedidoCompra pedido;
    private Produto produto;
    private BigDecimal quantidade;
    private double preco_unitario;
    private BigDecimal quantidade_recebida;
}
