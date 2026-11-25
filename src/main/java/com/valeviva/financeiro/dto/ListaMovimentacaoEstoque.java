package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.enums.TipoMovimentacao;
import com.valeviva.financeiro.model.MovimentacaoEstoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ListaMovimentacaoEstoque(
        Long id,
        TipoMovimentacao tipo,
        String codigoLote,
        String codigoProduto,
        String descricaoProduto,
        BigDecimal quantidade,
        LocalDateTime dataHora,
        String usuario,
        String origemDestino
) {
    public ListaMovimentacaoEstoque(MovimentacaoEstoque mov) {
        this(
                mov.getId(),
                mov.getTipo(),
                String.valueOf(mov.getLoteId()),
                mov.getProduto().getCodigo(),
                mov.getProduto().getNome(),
                mov.getQuantidade(),
                mov.getDataHora(),
                String.valueOf(mov.getUsuarioId()),
                mov.getOrigemDestino()
        );
    }
}
