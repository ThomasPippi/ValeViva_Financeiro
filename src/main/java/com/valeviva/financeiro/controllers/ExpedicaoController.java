package com.valeviva.financeiro.controller;

import com.valeviva.financeiro.model.Expedicao;
import com.valeviva.financeiro.service.ExpedicaoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/expedicoes")
public class ExpedicaoController {

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