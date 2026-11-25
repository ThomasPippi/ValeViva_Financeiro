package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.enums.StatusPedidoCompra;
import com.valeviva.financeiro.model.Fornecedor;
import com.valeviva.financeiro.model.PedidoCompra;

import java.time.LocalDate;
import java.util.List;

public record ListaPedidoCompra(
        Long id,
        Long fornecedor_id,
        String fornecedor_nome,
        LocalDate data_pedido,
        LocalDate data_prometida,
        LocalDate data_recebimento,
        StatusPedidoCompra status,
        List<ListaItemPedidoCompra> itens
) {
    public ListaPedidoCompra(PedidoCompra pedido) {
        this(
                pedido.getId(),
                pedido.getFornecedor().getId(),
                nomeDoFornecedor(pedido.getFornecedor()),
                pedido.getData_pedido(),
                pedido.getData_prometida(),
                pedido.getData_recebimento(),
                pedido.getStatus(),
                pedido.getItens().stream().map(ListaItemPedidoCompra::new).toList()
        );
    }

    // usa o nome fantasia, e se não tiver usa a razão social
    private static String nomeDoFornecedor(Fornecedor fornecedor) {
        if (fornecedor.getNome_fantasia() != null && !fornecedor.getNome_fantasia().isBlank()) {
            return fornecedor.getNome_fantasia();
        }
        return fornecedor.getRazao_social();
    }
}
