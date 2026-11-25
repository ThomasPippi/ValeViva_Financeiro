package com.valeviva.financeiro.service;

import com.valeviva.financeiro.model.Expedicao;
import com.valeviva.financeiro.model.SaldoEstoque;
import com.valeviva.financeiro.repository.ExpedicaoRepository;
import com.valeviva.financeiro.repository.SaldoEstoqueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class ExpedicaoService {

    private final ExpedicaoRepository expedicaoRepository;
    private final SaldoEstoqueRepository saldoRepository;
    private final LoteService loteService;

    public ExpedicaoService(
            ExpedicaoRepository expedicaoRepository,
            SaldoEstoqueRepository saldoRepository,
            LoteService loteService) {

        this.expedicaoRepository = expedicaoRepository;
        this.saldoRepository = saldoRepository;
        this.loteService = loteService;
    }

    @Transactional
    public Expedicao registrarExpedicao(
            Long produtoId,
            Long loteId,
            BigDecimal quantidade,
            String destino,
            Long usuarioId) {

        if (destino == null || destino.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "O destino da expedição deve ser informado."
            );
        }

        // Bloqueia (HTTP 422) lote vencido, não APROVADO ou ANVISA vencida
        loteService.validarLoteParaExpedicao(loteId);

        SaldoEstoque saldo = saldoRepository
                .findByProdutoIdAndLoteId(produtoId, loteId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Saldo de estoque não encontrado para o produto e lote informados."
                        ));

        try {
            saldo.baixa(quantidade);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        }

        saldoRepository.save(saldo);

        Expedicao expedicao = new Expedicao();
        expedicao.setLoteId(loteId);
        expedicao.setQuantidade(quantidade);
        expedicao.setDestino(destino.trim());
        expedicao.setUsuarioId(usuarioId);

        return expedicaoRepository.save(expedicao);
    }
}