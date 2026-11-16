package com.valeviva.financeiro.dto;

public record CadastroFornecedor(
        String razao_social,
        String nome_fantasia,
        String cnpj,
        String email,
        String telefone,
        int prazo_entrega_dias,
        boolean is_ativo
) {
}
