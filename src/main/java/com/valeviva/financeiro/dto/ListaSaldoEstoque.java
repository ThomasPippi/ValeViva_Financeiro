package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.model.Lote;
import com.valeviva.financeiro.model.SaldoEstoque;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ListaSaldoEstoque(
        Long id,
        Long produtoId,
        String codigoProduto,
        String descricaoProduto,
        String unidade,
        int estoqueMinimo,
        Long loteId,
        String codigoLote,
        LocalDate validade,
        String statusQualidade,
        BigDecimal quantidade
) {
    public ListaSaldoEstoque(SaldoEstoque saldo, Lote lote) {
        this(
                saldo.getId(),
                saldo.getProduto().getId(),
                saldo.getProduto().getCodigo(),
                saldo.getProduto().getNome(),
                saldo.getProduto().getUnidade(),
                saldo.getProduto().getEstoque_minimo(),
                saldo.getLoteId(),
                lote != null ? lote.getCodigo() : String.valueOf(saldo.getLoteId()),
                lote != null ? lote.getDataValidade() : null,
                lote != null && lote.getStatusQualidade() != null ? lote.getStatusQualidade().name() : null,
                saldo.getQuantidade()
        );
    }
}