package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.enums.TipoProduto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CadastroProduto(
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
) {}
