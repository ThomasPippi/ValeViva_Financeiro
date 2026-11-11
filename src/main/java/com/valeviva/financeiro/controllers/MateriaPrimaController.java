package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroMateriaPrima;
import com.valeviva.financeiro.model.MateriaPrima;
import com.valeviva.financeiro.repository.MateriaPrimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materias-primas")
public class MateriaPrimaController {

    @Autowired
    private MateriaPrimaRepository repository;

    // Endpoint para Cadastrar (POST)
    @PostMapping
    public ResponseEntity<MateriaPrima> cadastrar(@RequestBody CadastroMateriaPrima dto) {
        // Cria a entidade usando o construtor com o DTO e salva no banco
        MateriaPrima materiaPrima = new MateriaPrima(dto);
        MateriaPrima novaMateriaPrima = repository.save(materiaPrima);

        // Retorna status 201 (Created) com o objeto salvo
        return ResponseEntity.status(HttpStatus.CREATED).body(novaMateriaPrima);
    }

    // Endpoint para Listar (GET)
    @GetMapping
    public ResponseEntity<List<MateriaPrima>> listar() {
        List<MateriaPrima> lista = repository.findAll();
        return ResponseEntity.ok(lista); // Retorna status 200 (OK) com a lista
    }
}
