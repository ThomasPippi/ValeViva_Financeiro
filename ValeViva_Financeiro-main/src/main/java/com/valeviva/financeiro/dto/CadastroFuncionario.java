package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.enums.Setor;

public record CadastroFuncionario (
    String nome,
    String matricula,
    String cargo,
    Setor setor,
    boolean is_ativo
) {}
