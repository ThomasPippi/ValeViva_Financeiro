package com.valeviva.financeiro.dto;

// DTOs servem apenas para carregar dados imutáveis
public record CadastroMateriaPrima(
        String nome,
        String descricao,
        double custoUnitario,
        double quantidadeEstoque,
        String unidadeMedida
) {
}
