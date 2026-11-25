package com.valeviva.financeiro.dto;

import java.time.LocalDate;
import java.util.List;

public record CadastroPedidoCompra(
        Long fornecedor_id,
        LocalDate data_pedido,
        LocalDate data_prometida,
        List<CadastroItemPedidoCompra> itens
) {}
