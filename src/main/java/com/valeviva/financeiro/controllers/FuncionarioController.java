package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroFuncionario;
import com.valeviva.financeiro.dto.ListaFuncionario;
import com.valeviva.financeiro.model.Funcionario;
import com.valeviva.financeiro.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @PostMapping
    public void newFuncionario(@RequestBody CadastroFuncionario funcionario) {
        funcionarioRepository.save(new Funcionario(funcionario));
    }

    @GetMapping
    public Page<ListaFuncionario> listFuncionario(Pageable pageable) {
        return funcionarioRepository.findAll(pageable).map(ListaFuncionario::new);
    }
}
