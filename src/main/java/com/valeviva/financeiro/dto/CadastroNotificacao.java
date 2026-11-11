package com.valeviva.financeiro.dto;

import jakarta.validation.constraints.NotBlank;

public record CadastroNotificacao(
        @NotBlank(message = "O título é obrigatório")
        String titulo,

        @NotBlank(message = "A mensagem é obrigatória")
        String mensagem
) {}
