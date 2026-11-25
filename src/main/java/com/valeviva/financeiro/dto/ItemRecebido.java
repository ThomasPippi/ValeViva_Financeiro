package com.valeviva.financeiro.dto;

import java.math.BigDecimal;

public record ItemRecebido(
        Long item_id,
        BigDecimal quantidade_recebida
) {}
