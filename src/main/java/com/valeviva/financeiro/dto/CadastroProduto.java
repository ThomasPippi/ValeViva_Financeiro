package com.valeviva.financeiro.dto;

public record CadastroProduto(
     String nome,
     String descricao,
     double preco,
     int quantidade_estoque,
     String categoria
) {}
