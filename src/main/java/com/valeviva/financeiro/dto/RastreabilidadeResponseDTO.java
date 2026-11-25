package com.valeviva.financeiro.dto;

import java.math.BigDecimal;
import java.util.List;

public record RastreabilidadeResponseDTO(
        LoteResponseDTO lote,
        BigDecimal saldoAtual,
        List<MovimentacaoRastreioDTO> movimentacoes
) {}
