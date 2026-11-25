package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.enums.TipoProduto;

public record CadastroProduto(
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
) {}
