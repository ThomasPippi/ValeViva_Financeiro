package com.valeviva.financeiro.dto;

import java.time.LocalDate;

public record LoteRequestDTO(
        String codigo,
        Long produtoId,
        Long ordemOrigemId,
        LocalDate dataFabricacao,
        LocalDate dataValidade
) {}
