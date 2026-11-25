package com.valeviva.financeiro.dto;

import java.math.BigDecimal;

public record CadastroExpedicao(
        Long produtoId,
        Long loteId,
        BigDecimal quantidade,
        String destino,
        Long usuarioId
) {}
