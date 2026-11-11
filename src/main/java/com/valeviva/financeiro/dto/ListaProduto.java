package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.model.Produto;

public record ListaProduto (
        Long id,
        String nome,
        String descricao,
        double preco,
        int quantidade_estoque,
        String categoria
) {
    public ListaProduto(Produto produto) {
        this(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade_estoque(), // <-- Ajustado aqui (E maiúsculo)
                produto.getCategoria()
        );
    }
}
