package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroExpedicao;
import com.valeviva.financeiro.model.Expedicao;
import com.valeviva.financeiro.service.ExpedicaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/expedicoes")
public class ExpedicaoController {

    @Autowired
    private ExpedicaoService expedicaoService;

    @PostMapping
    public ResponseEntity<?> registrarExpedicao(@RequestBody CadastroExpedicao dados) {
        if (dados.produtoId() == null || dados.loteId() == null || dados.usuarioId() == null) {
            return ResponseEntity.badRequest().body("Produto, lote e usuário são obrigatórios.");
        }

        try {
            Expedicao expedicao = expedicaoService.registrarExpedicao(
                    dados.produtoId(),
                    dados.loteId(),
                    dados.quantidade(),
                    dados.destino(),
                    dados.usuarioId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(expedicao);
        } catch (ResponseStatusException e) {
            String mensagem = e.getReason() != null ? e.getReason() : "Operação não permitida.";
            return ResponseEntity.status(e.getStatusCode()).body(mensagem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
