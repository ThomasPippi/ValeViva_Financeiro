package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroProduto;
import com.valeviva.financeiro.dto.ListaProduto;
import com.valeviva.financeiro.model.Produto;
import com.valeviva.financeiro.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    public void newProduto(@RequestBody CadastroProduto produto){
        produtoRepository.save(new Produto(produto));
    }

    @GetMapping
    public Page<ListaProduto> listProduto(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(ListaProduto::new);
    }
}
