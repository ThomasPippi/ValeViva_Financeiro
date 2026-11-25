package com.valeviva.financeiro.dto;

import java.math.BigDecimal;

public record CadastroEntradaEstoque(
        Long produtoId,
        Long loteId,
        BigDecimal quantidade,
        Long usuarioId,
        String origemDestino
) {}
