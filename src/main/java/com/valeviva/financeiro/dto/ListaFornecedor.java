package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.model.Fornecedor;

public record ListaFornecedor(
        Long id,
        String razao_social,
        String nome_fantasia,
        String cnpj,
        String email,
        String telefone,
        int prazo_entrega_dias,
        boolean is_ativo
) {
    public ListaFornecedor(Fornecedor fornecedor) {
        this(fornecedor.getId(), fornecedor.getRazao_social(), fornecedor.getNome_fantasia(), fornecedor.getCnpj(), fornecedor.getEmail(), fornecedor.getTelefone(), fornecedor.getPrazo_entrega_dias(),  fornecedor.is_ativo());
    }

}
