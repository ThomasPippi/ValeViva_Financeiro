package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroProduto;
import com.valeviva.financeiro.dto.ListaProduto;
import com.valeviva.financeiro.model.Produto;
import com.valeviva.financeiro.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    @Transactional
    public void newProduto(@RequestBody CadastroProduto produto){
        produtoRepository.save(new Produto(produto));
    }

    @GetMapping
    public Page<ListaProduto> listProdutos(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(ListaProduto::new);
    }

    @GetMapping("/buscar")
    public Page<ListaProduto> listarProdutosMarcaPorBuscar(@RequestParam String nome, String codigo, Pageable pageable) {
        return produtoRepository.findByNomeAndCodigoContainingIgnoreCase(nome, codigo, pageable).map(ListaProduto::new);
    }

    @GetMapping("{id}")
    public Object findProduto(@PathVariable Long id) {
        return produtoRepository.findById(id);
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<?> atualizarProduto(@PathVariable Long id, @RequestBody CadastroProduto dados) {
        Produto produto = produtoRepository.findById(id).orElse(null);
        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        produto.atualizar(dados);
        return ResponseEntity.ok(new ListaProduto(produto));
    }

    // Apaga o produto; o banco apaga em cascata itens de pedido, OPs, lotes,
    // saldos, movimentações, BOM e roteiros ligados a ele (migration V16)
    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<?> excluirProduto(@PathVariable Long id) {
        if (!produtoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
