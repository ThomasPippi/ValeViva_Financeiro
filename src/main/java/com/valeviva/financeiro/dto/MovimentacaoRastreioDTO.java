package com.valeviva.financeiro.dto;

public record MovimentacaoRastreioDTO(
        Long id,
        String tipo,
        String quantidade,
        String dataHora,
        String usuario
) {}
