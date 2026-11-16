package com.valeviva.financeiro.model;

import com.valeviva.financeiro.enums.StatusPedidoCompra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoCompra {
    private Long id;
    private Fornecedor fornecedor;
    private LocalDate data_pedido;
    private LocalDate data_prometida;
    private LocalDate data_recebimento;
    private StatusPedidoCompra status;
    private Long ordem_producao_id;
    private List<ItemPedidoCompra> itens = new ArrayList<>();
}
