package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.*;
import com.valeviva.financeiro.enums.StatusPedidoCompra;
import com.valeviva.financeiro.model.Fornecedor;
import com.valeviva.financeiro.model.ItemPedidoCompra;
import com.valeviva.financeiro.model.PedidoCompra;
import com.valeviva.financeiro.model.Produto;
import com.valeviva.financeiro.repository.FornecedorRepository;
import com.valeviva.financeiro.repository.PedidoCompraRepository;
import com.valeviva.financeiro.repository.ProdutoRepository;
import com.valeviva.financeiro.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/pedidos-compra")
public class PedidoCompraController {

    @Autowired
    private PedidoCompraRepository pedidoCompraRepository;

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public Page<ListaPedidoCompra> listarPedidos(Pageable pageable) {
        return pedidoCompraRepository.findAll(pageable).map(ListaPedidoCompra::new);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> buscarPedido(@PathVariable Long id) {
        PedidoCompra pedido = pedidoCompraRepository.findById(id).orElse(null);
        if (pedido == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }
        return ResponseEntity.ok(new ListaPedidoCompra(pedido));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> novoPedido(@RequestBody CadastroPedidoCompra dados) {
        Fornecedor fornecedor = fornecedorRepository.findById(dados.fornecedor_id()).orElse(null);
        if (fornecedor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fornecedor não encontrado.");
        }

        if (dados.itens() == null || dados.itens().isEmpty()) {
            return ResponseEntity.badRequest().body("O pedido precisa ter pelo menos um item.");
        }

        PedidoCompra pedido = new PedidoCompra();
        pedido.setFornecedor(fornecedor);
        pedido.setData_prometida(dados.data_prometida());
        pedido.setStatus(StatusPedidoCompra.ABERTO);

        if (dados.data_pedido() != null) {
            pedido.setData_pedido(dados.data_pedido());
        } else {
            pedido.setData_pedido(LocalDate.now());
        }

        for (CadastroItemPedidoCompra itemDados : dados.itens()) {
            Produto produto = produtoRepository.findById(itemDados.produto_id()).orElse(null);
            if (produto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Produto " + itemDados.produto_id() + " não encontrado.");
            }

            ItemPedidoCompra item = new ItemPedidoCompra();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDados.quantidade());
            item.setPreco_unitario(itemDados.preco_unitario());
            pedido.getItens().add(item);
        }

        PedidoCompra salvo = pedidoCompraRepository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ListaPedidoCompra(salvo));
    }

    @PutMapping("{id}/recebimento")
    @Transactional
    public ResponseEntity<?> receberPedido(@PathVariable Long id, @RequestBody RecebimentoPedidoCompra dados) {
        PedidoCompra pedido = pedidoCompraRepository.findById(id).orElse(null);
        if (pedido == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }

        if (pedido.getStatus() != StatusPedidoCompra.ABERTO) {
            return ResponseEntity.badRequest().body("Só é possível receber pedidos em aberto.");
        }

        if (dados.itens() != null) {
            for (ItemRecebido recebido : dados.itens()) {
                for (ItemPedidoCompra item : pedido.getItens()) {
                    if (item.getId().equals(recebido.item_id())) {
                        BigDecimal quantidade = recebido.quantidade_recebida();
                        if (quantidade == null) {
                            quantidade = BigDecimal.ZERO;
                        }
                        if (quantidade.compareTo(BigDecimal.ZERO) < 0) {
                            return ResponseEntity.badRequest()
                                    .body("Quantidade recebida não pode ser negativa.");
                        }

                        item.setQuantidade_recebida(quantidade);

                        if (quantidade.compareTo(BigDecimal.ZERO) > 0) {
                            estoqueService.registrarEntrada(
                                    item.getProduto().getId(),
                                    1L,
                                    quantidade,
                                    1L,
                                    "Pedido de compra #" + pedido.getId()
                            );
                        }
                    }
                }
            }
        }

        if (dados.data_recebimento() != null) {
            pedido.setData_recebimento(dados.data_recebimento());
        } else {
            pedido.setData_recebimento(LocalDate.now());
        }
        pedido.setStatus(StatusPedidoCompra.RECEBIDO);

        PedidoCompra salvo = pedidoCompraRepository.save(pedido);
        return ResponseEntity.ok(new ListaPedidoCompra(salvo));
    }
}
