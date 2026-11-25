package com.valeviva.financeiro.dto;

import java.math.BigDecimal;

public record CadastroItemPedidoCompra(
        Long produto_id,
        BigDecimal quantidade,
        BigDecimal preco_unitario
) {}
