package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroFornecedor;
import com.valeviva.financeiro.dto.ListaFornecedor;
import com.valeviva.financeiro.model.Fornecedor;
import com.valeviva.financeiro.repository.FornecedorRepository;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @GetMapping
    public Page<ListaFornecedor> listFornecedor(Pageable pageable) {
        return fornecedorRepository.findAll(pageable).map(ListaFornecedor::new);
    }

    @PostMapping
    public void newFornecedor(@RequestBody CadastroFornecedor fornecedor) {
        fornecedorRepository.save(new Fornecedor(fornecedor));
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<?> atualizarFornecedor(@PathVariable Long id, @RequestBody CadastroFornecedor dados) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElse(null);
        if (fornecedor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fornecedor não encontrado.");
        }
        fornecedor.atualizar(dados);
        return ResponseEntity.ok(new ListaFornecedor(fornecedor));
    }

    // Apaga o fornecedor; o banco apaga em cascata os pedidos de compra dele (migration V16)
    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<?> excluirFornecedor(@PathVariable Long id) {
        if (!fornecedorRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fornecedor não encontrado.");
        }
        fornecedorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
