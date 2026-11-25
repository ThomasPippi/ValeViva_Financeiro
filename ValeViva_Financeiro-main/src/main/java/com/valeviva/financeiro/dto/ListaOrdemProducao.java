package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.model.OrdemProducao;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ListaOrdemProducao(
        Long id,
        Long produtoId,
        String codigoProduto,
        String descricaoProduto,
        Integer quantidade,
        LocalDate prazo,
        Integer prioridade,
        String status,
        LocalDateTime emitidaEm
) {
    public ListaOrdemProducao(OrdemProducao ordem) {
        this(
                ordem.getId(),
                ordem.getProduto().getId(),
                ordem.getProduto().getCodigo(),
                ordem.getProduto().getNome(),
                ordem.getQuantidade(),
                ordem.getPrazo(),
                ordem.getPrioridade(),
                ordem.getStatus(),
                ordem.getDataCriacao()
        );
    }
}
