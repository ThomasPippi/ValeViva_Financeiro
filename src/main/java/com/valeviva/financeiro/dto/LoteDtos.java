package com.valeviva.financeiro.dto.lote;

import com.valeviva.financeiro.enums.StatusQualidade;

import java.time.LocalDate;
import java.util.List;

public record LoteRequestDTO(
        String codigo,
        Long produtoId,
        Long ordemOrigemId,
        LocalDate dataFabricacao,
        LocalDate dataValidade
) {}

public record QualidadeUpdateDTO(
        StatusQualidade statusQualidade,
        String observacao
) {}

public record LoteResponseDTO(
        Long id,
        String codigo,
        Long produtoId,
        String produtoNome,
        String registroAnvisa,
        String vencimentoAnvisa,
        Long ordemOrigemId,
        LocalDate dataFabricacao,
        LocalDate dataValidade,
        boolean vencido,
        boolean anvisaVencida,
        StatusQualidade statusQualidade,
        String observacaoQualidade
) {}

public record MovimentacaoRastreioDTO(
        Long id,
        String tipo,
        String quantidade,
        String dataHora,
        String usuario
) {}

public record RastreabilidadeResponseDTO(
        LoteResponseDTO lote,
        List<MovimentacaoRastreioDTO> movimentacoes
) {}