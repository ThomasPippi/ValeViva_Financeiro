package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.enums.Setor;
import com.valeviva.financeiro.model.Funcionario;

public record ListaFuncionario(
        String nome,
        String matricula,
        String cargo,
        Setor setor,
        boolean is_ativo
) {
    public ListaFuncionario(Funcionario funcionario) {
        this(funcionario.getNome(), funcionario.getMatricula(), funcionario.getCargo(), funcionario.getSetor(), funcionario.is_ativo());
    }
}
