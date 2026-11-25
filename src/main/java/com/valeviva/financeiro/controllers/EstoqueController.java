<<<<<<< HEAD
package com.valeviva.financeiro.controller;

=======
package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroEntradaEstoque;
import com.valeviva.financeiro.dto.ListaMovimentacaoEstoque;
import com.valeviva.financeiro.dto.ListaSaldoEstoque;
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
import com.valeviva.financeiro.model.MovimentacaoEstoque;
import com.valeviva.financeiro.model.SaldoEstoque;
import com.valeviva.financeiro.repository.MovimentacaoEstoqueRepository;
import com.valeviva.financeiro.repository.SaldoEstoqueRepository;
import com.valeviva.financeiro.service.EstoqueService;
<<<<<<< HEAD
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

<<<<<<< HEAD
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
=======
    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private SaldoEstoqueRepository saldoRepository;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoRepository;

    @GetMapping("/saldos")
    public List<ListaSaldoEstoque> listarSaldos() {
        return saldoRepository.findAll().stream().map(ListaSaldoEstoque::new).toList();
    }

    // filtros opcionais: data de início, data de fim e código/nome do produto
    @GetMapping("/movimentacoes")
    public List<ListaMovimentacaoEstoque> listarMovimentacoes(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fim,
            @RequestParam(required = false) String produto) {

        List<ListaMovimentacaoEstoque> resultado = new ArrayList<>();

        for (MovimentacaoEstoque mov : movimentacaoRepository.findAll()) {
            LocalDate data = mov.getDataHora().toLocalDate();

            if (inicio != null && data.isBefore(inicio)) continue;
            if (fim != null && data.isAfter(fim)) continue;

            if (produto != null && !produto.isBlank()) {
                String busca = produto.toLowerCase();
                String codigo = mov.getProduto().getCodigo().toLowerCase();
                String nome = mov.getProduto().getNome().toLowerCase();
                if (!codigo.contains(busca) && !nome.contains(busca)) continue;
            }

            resultado.add(new ListaMovimentacaoEstoque(mov));
        }

        return resultado;
    }

    @PostMapping("/entradas")
    public ResponseEntity<?> registrarEntrada(@RequestBody CadastroEntradaEstoque dados) {
        if (dados.produtoId() == null || dados.loteId() == null || dados.usuarioId() == null) {
            return ResponseEntity.badRequest().body("Produto, lote e usuário são obrigatórios.");
        }

        try {
            SaldoEstoque saldo = estoqueService.registrarEntrada(
                    dados.produtoId(),
                    dados.loteId(),
                    dados.quantidade(),
                    dados.usuarioId(),
                    dados.origemDestino()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(new ListaSaldoEstoque(saldo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
