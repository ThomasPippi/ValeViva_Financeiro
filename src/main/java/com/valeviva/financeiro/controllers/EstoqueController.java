package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroEntradaEstoque;
import com.valeviva.financeiro.dto.ListaMovimentacaoEstoque;
import com.valeviva.financeiro.dto.ListaSaldoEstoque;
import com.valeviva.financeiro.model.Lote;
import com.valeviva.financeiro.model.MovimentacaoEstoque;
import com.valeviva.financeiro.model.SaldoEstoque;
import com.valeviva.financeiro.repository.LoteRepository;
import com.valeviva.financeiro.repository.MovimentacaoEstoqueRepository;
import com.valeviva.financeiro.repository.SaldoEstoqueRepository;
import com.valeviva.financeiro.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private SaldoEstoqueRepository saldoRepository;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoRepository;

    @Autowired
    private LoteRepository loteRepository;

    @GetMapping("/saldos")
    public List<ListaSaldoEstoque> listarSaldos() {
        return saldoRepository.findAll().stream()
                .map(saldo -> new ListaSaldoEstoque(
                        saldo,
                        saldo.getLoteId() != null
                                ? loteRepository.findById(saldo.getLoteId()).orElse(null)
                                : null
                ))
                .toList();
    }

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
            Lote lote = loteRepository.findById(dados.loteId()).orElse(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ListaSaldoEstoque(saldo, lote));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/saldos/{id}")
    public ResponseEntity<?> atualizarSaldo(
            @PathVariable Long id,
            @RequestBody CadastroEntradaEstoque dados) {

        return saldoRepository.findById(id)
                .map(saldo -> {
                    if (dados.quantidade() != null) {
                        saldo.setQuantidade(dados.quantidade());
                    }
                    if (dados.loteId() != null) {
                        saldo.setLoteId(dados.loteId());
                    }
                    SaldoEstoque salvo = saldoRepository.save(saldo);
                    Lote lote = salvo.getLoteId() != null
                            ? loteRepository.findById(salvo.getLoteId()).orElse(null)
                            : null;
                    return ResponseEntity.ok(new ListaSaldoEstoque(salvo, lote));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Saldo de estoque não encontrado."));
    }

    @DeleteMapping("/saldos/{id}")
    public ResponseEntity<?> excluirSaldo(@PathVariable Long id) {
        if (!saldoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Saldo de estoque não encontrado.");
        }
        saldoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}