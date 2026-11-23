package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.enums.TipoProduto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.valeviva.financeiro.model.Produto;

public record ListaProduto (
        Long id,
        String nome,
        String codigo,
        String descricao,
        TipoProduto tipo,
        String unidade,
        BigDecimal preco,
        int quantidade_estoque,
        int estoque_minimo,
        String registro_anvisa,
        LocalDate vencimento_anvisa,
        boolean is_ativo
) {
    public ListaProduto(Produto produto) {
        this(
                produto.getId(),
                produto.getNome(),
                produto.getCodigo(),
                produto.getDescricao(),
                produto.getTipo(),
                produto.getUnidade(),
                produto.getPreco(),
                produto.getQuantidade_estoque(),
                produto.getEstoque_minimo(),
                produto.getRegistro_anvisa(),
                produto.getVencimento_anvisa(),
                produto.is_ativo()
        );
    }
}
