package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.enums.StatusQualidade;

import java.time.LocalDate;

public record LoteResponseDTO(
        Long id,
        String codigo,
        Long produtoId,
        String produtoNome,
        String registroAnvisa,
        LocalDate vencimentoAnvisa,
        Long ordemOrigemId,
        LocalDate dataFabricacao,
        LocalDate dataValidade,
        boolean vencido,
        boolean anvisaVencida,
        StatusQualidade statusQualidade,
        String observacaoQualidade
) {}
