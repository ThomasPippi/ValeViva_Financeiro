<<<<<<< HEAD
package com.valeviva.financeiro.controller;

import com.valeviva.financeiro.model.Expedicao;
import com.valeviva.financeiro.service.ExpedicaoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

=======
package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroExpedicao;
import com.valeviva.financeiro.model.Expedicao;
import com.valeviva.financeiro.service.ExpedicaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
@RestController
@RequestMapping("/expedicoes")
public class ExpedicaoController {

<<<<<<< HEAD
    private final ExpedicaoService expedicaoService;

    public ExpedicaoController(ExpedicaoService expedicaoService) {
        this.expedicaoService = expedicaoService;
    }

    public record ExpedicaoRequest(
            Long produtoId,
            Long loteId,
            BigDecimal quantidade,
            String destino,
            Long usuarioId
    ) {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expedicao criar(@RequestBody ExpedicaoRequest req) {
        return expedicaoService.registrarExpedicao(
                req.produtoId(),
                req.loteId(),
                req.quantidade(),
                req.destino(),
                req.usuarioId()
        );
    }
}
=======
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
