package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroProduto;
import com.valeviva.financeiro.dto.ListaProduto;
import com.valeviva.financeiro.model.Produto;
import com.valeviva.financeiro.repository.ProdutoRepository;
<<<<<<< HEAD
=======
import jakarta.transaction.Transactional;
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
<<<<<<< HEAD
@RequestMapping("/produto")
=======
@RequestMapping("/produtos")
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
<<<<<<< HEAD
=======
    @Transactional
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
    public void newProduto(@RequestBody CadastroProduto produto){
        produtoRepository.save(new Produto(produto));
    }

    @GetMapping
<<<<<<< HEAD
    public Page<ListaProduto> listProduto(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(ListaProduto::new);
    }
=======
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
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
}
