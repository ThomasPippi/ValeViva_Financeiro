package com.valeviva.financeiro.dto;

import java.time.LocalDate;
import java.util.List;

public record RecebimentoPedidoCompra(
        LocalDate data_recebimento,
        List<ItemRecebido> itens
) {}
