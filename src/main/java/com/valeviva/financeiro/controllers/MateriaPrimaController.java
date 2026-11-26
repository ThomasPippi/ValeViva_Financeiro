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

    @PostMapping
    public ResponseEntity<MateriaPrima> cadastrar(@RequestBody CadastroMateriaPrima dto) {
        MateriaPrima materiaPrima = new MateriaPrima(dto);
        MateriaPrima novaMateriaPrima = repository.save(materiaPrima);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaMateriaPrima);
    }

    @GetMapping
    public ResponseEntity<List<MateriaPrima>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MateriaPrima> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MateriaPrima> atualizar(
            @PathVariable Long id,
            @RequestBody CadastroMateriaPrima dto
    ) {
        return repository.findById(id)
                .map(mp -> {
                    mp.setNome(dto.nome());
                    mp.setDescricao(dto.descricao());
                    mp.setCustoUnitario(dto.custoUnitario());
                    mp.setQuantidadeEstoque(dto.quantidadeEstoque());
                    mp.setUnidadeMedida(dto.unidadeMedida());
                    return ResponseEntity.ok(repository.save(mp));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
