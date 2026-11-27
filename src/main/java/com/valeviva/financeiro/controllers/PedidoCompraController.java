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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pedidos-compra")
public class PedidoCompraController {

    @Autowired
    private PedidoCompraRepository pedidoCompraRepository;

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

        // se não mandar a data do pedido, usa a data de hoje
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

        for (ItemRecebido recebido : dados.itens()) {
            // procura o item dentro do pedido
            for (ItemPedidoCompra item : pedido.getItens()) {
                if (item.getId().equals(recebido.item_id())) {
                    BigDecimal quantidade = recebido.quantidade_recebida();
                    if (quantidade == null) {
                        quantidade = BigDecimal.ZERO;
                    }
                    item.setQuantidade_recebida(quantidade);

                    // soma o que chegou no estoque do produto
                    Produto produto = item.getProduto();
                    produto.setQuantidade_estoque(produto.getQuantidade_estoque() + quantidade.intValue());
                }
            }
        }

        if (dados.data_recebimento() != null) {
            pedido.setData_recebimento(dados.data_recebimento());
        } else {
            pedido.setData_recebimento(LocalDate.now());
        }
        pedido.setStatus(StatusPedidoCompra.RECEBIDO);

        return ResponseEntity.ok(new ListaPedidoCompra(pedido));
    }

    // Só pedidos em aberto podem ser alterados; os itens são substituídos pelos enviados
    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<?> atualizarPedido(@PathVariable Long id, @RequestBody CadastroPedidoCompra dados) {
        PedidoCompra pedido = pedidoCompraRepository.findById(id).orElse(null);
        if (pedido == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }

        if (pedido.getStatus() != StatusPedidoCompra.ABERTO) {
            return ResponseEntity.badRequest().body("Só é possível alterar pedidos em aberto.");
        }

        Fornecedor fornecedor = fornecedorRepository.findById(dados.fornecedor_id()).orElse(null);
        if (fornecedor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fornecedor não encontrado.");
        }

        if (dados.itens() == null || dados.itens().isEmpty()) {
            return ResponseEntity.badRequest().body("O pedido precisa ter pelo menos um item.");
        }

        // valida todos os produtos antes de mexer no pedido
        List<ItemPedidoCompra> novosItens = new ArrayList<>();
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
            novosItens.add(item);
        }

        pedido.setFornecedor(fornecedor);
        pedido.setData_prometida(dados.data_prometida());
        if (dados.data_pedido() != null) {
            pedido.setData_pedido(dados.data_pedido());
        }

        // orphanRemoval apaga do banco os itens que saírem da lista
        pedido.getItens().clear();
        pedido.getItens().addAll(novosItens);

        PedidoCompra salvo = pedidoCompraRepository.save(pedido);
        return ResponseEntity.ok(new ListaPedidoCompra(salvo));
    }

    // Apaga o pedido e seus itens, em qualquer status
    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<?> excluirPedido(@PathVariable Long id) {
        if (!pedidoCompraRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }
        pedidoCompraRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
