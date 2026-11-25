package com.valeviva.financeiro.controller;

import com.valeviva.financeiro.model.MovimentacaoEstoque;
import com.valeviva.financeiro.model.SaldoEstoque;
import com.valeviva.financeiro.repository.MovimentacaoEstoqueRepository;
import com.valeviva.financeiro.repository.SaldoEstoqueRepository;
import com.valeviva.financeiro.service.EstoqueService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;
    private final SaldoEstoqueRepository saldoEstoqueRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public EstoqueController(EstoqueService estoqueService,
                             SaldoEstoqueRepository saldoEstoqueRepository,
                             MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.estoqueService = estoqueService;
        this.saldoEstoqueRepository = saldoEstoqueRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    public record MovimentacaoEstoqueRequest(
            Long produtoId,
            Long loteId,
            BigDecimal quantidade,
            Long usuarioId,
            String origemDestino
    ) {}

    @GetMapping("/saldos")
    public List<SaldoEstoque> listarSaldos() {
        return saldoEstoqueRepository.findAll();
    }

    @GetMapping("/movimentacoes")
    public List<MovimentacaoEstoque> listarMovimentacoes() {
        return movimentacaoEstoqueRepository.findAll();
    }

    @PostMapping("/entradas")
    @ResponseStatus(HttpStatus.CREATED)
    public SaldoEstoque entrada(@RequestBody MovimentacaoEstoqueRequest req) {
        return estoqueService.registrarEntrada(
                req.produtoId(),
                req.loteId(),
                req.quantidade(),
                req.usuarioId(),
                req.origemDestino()
        );
    }

    @PostMapping("/saidas")
    @ResponseStatus(HttpStatus.CREATED)
    public SaldoEstoque saida(@RequestBody MovimentacaoEstoqueRequest req) {
        return estoqueService.registrarSaida(
                req.produtoId(),
                req.loteId(),
                req.quantidade(),
                req.usuarioId(),
                req.origemDestino()
        );
    }
}