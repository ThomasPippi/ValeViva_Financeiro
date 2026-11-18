package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroProduto;
import com.valeviva.financeiro.dto.ListaProduto;
import com.valeviva.financeiro.model.Produto;
import com.valeviva.financeiro.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}
