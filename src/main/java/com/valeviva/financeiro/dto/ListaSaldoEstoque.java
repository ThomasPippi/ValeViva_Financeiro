package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.model.SaldoEstoque;

import java.math.BigDecimal;

public record ListaSaldoEstoque(
        Long id,
        Long produtoId,
        String codigoProduto,
        String descricaoProduto,
        String unidade,
        int estoqueMinimo,
        Long loteId,
        String codigoLote,
        BigDecimal quantidade
) {
    public ListaSaldoEstoque(SaldoEstoque saldo) {
        this(
                saldo.getId(),
                saldo.getProduto().getId(),
                saldo.getProduto().getCodigo(),
                saldo.getProduto().getNome(),
                saldo.getProduto().getUnidade(),
                saldo.getProduto().getEstoque_minimo(),
                saldo.getLoteId(),
                String.valueOf(saldo.getLoteId()),
                saldo.getQuantidade()
        );
    }
}
