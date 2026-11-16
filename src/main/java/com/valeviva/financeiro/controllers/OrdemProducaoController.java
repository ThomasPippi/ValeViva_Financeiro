package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroOrdemProducao;
import com.valeviva.financeiro.model.OrdemProducao;
import com.valeviva.financeiro.model.Produto;
import com.valeviva.financeiro.repository.OrdemProducaoRepository;
import com.valeviva.financeiro.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens-producao")
public class OrdemProducaoController {

    @Autowired
    private OrdemProducaoRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> criar(@RequestBody @Valid CadastroOrdemProducao dto) {
        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElse(null);

        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        OrdemProducao op = new OrdemProducao(produto, dto.quantidade());
        OrdemProducao novaOp = repository.save(op);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaOp);
    }

    @GetMapping
    public ResponseEntity<List<OrdemProducao>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }
}
