package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroFornecedor;
import com.valeviva.financeiro.dto.ListaFornecedor;
import com.valeviva.financeiro.model.Fornecedor;
import com.valeviva.financeiro.repository.FornecedorRepository;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void newFornecedor(CadastroFornecedor fornecedor) {
        fornecedorRepository.save(new Fornecedor(fornecedor));
    }
}
