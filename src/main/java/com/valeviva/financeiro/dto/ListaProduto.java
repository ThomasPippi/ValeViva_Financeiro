package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.enums.TipoProduto;
import com.valeviva.financeiro.model.Produto;

public record ListaProduto (
        Long id,
        String nome,
        String codigo,
        String descricao,
        TipoProduto tipo,
        String unidade,
        double preco,
        int quantidade_estoque,
        int estoque_minimo,
        String registro_anvisa,
        String vencimento_anvisa,
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
