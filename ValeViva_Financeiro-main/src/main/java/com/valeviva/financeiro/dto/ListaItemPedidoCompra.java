package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.model.ItemPedidoCompra;

import java.math.BigDecimal;

public record ListaItemPedidoCompra(
        Long id,
        Long produto_id,
        String produto_nome,
        BigDecimal quantidade,
        BigDecimal preco_unitario,
        BigDecimal quantidade_recebida
) {
    public ListaItemPedidoCompra(ItemPedidoCompra item) {
        this(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPreco_unitario(),
                item.getQuantidade_recebida()
        );
    }
}
