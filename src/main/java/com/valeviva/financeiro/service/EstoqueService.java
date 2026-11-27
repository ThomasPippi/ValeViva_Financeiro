package com.valeviva.financeiro.service;

import com.valeviva.financeiro.enums.TipoMovimentacao;
import com.valeviva.financeiro.model.MovimentacaoEstoque;
import com.valeviva.financeiro.model.Produto;
import com.valeviva.financeiro.model.SaldoEstoque;
import com.valeviva.financeiro.repository.MovimentacaoEstoqueRepository;
import com.valeviva.financeiro.repository.ProdutoRepository;
import com.valeviva.financeiro.repository.SaldoEstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class EstoqueService {

    private final ProdutoRepository produtoRepository;
    private final SaldoEstoqueRepository saldoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    public EstoqueService(
            ProdutoRepository produtoRepository,
            SaldoEstoqueRepository saldoRepository,
            MovimentacaoEstoqueRepository movimentacaoRepository) {
        this.produtoRepository = produtoRepository;
        this.saldoRepository = saldoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Transactional
    public SaldoEstoque registrarEntrada(
            Long produtoId,
            Long loteId,
            BigDecimal quantidade,
            Long usuarioId,
            String origemDestino) {

        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        SaldoEstoque saldo = saldoRepository
                .findByProdutoIdAndLoteId(produtoId, loteId)
                .orElseGet(() -> {
                    SaldoEstoque novoSaldo = new SaldoEstoque();
                    novoSaldo.setProduto(produto);
                    novoSaldo.setLoteId(loteId);
                    novoSaldo.setQuantidade(BigDecimal.ZERO);
                    return novoSaldo;
                });

        saldo.entrada(quantidade);
        saldoRepository.save(saldo);

        sincronizarSaldoProduto(produto);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTipo(TipoMovimentacao.ENTRADA);
        movimentacao.setProduto(produto);
        movimentacao.setLoteId(loteId);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setUsuarioId(usuarioId);
        movimentacao.setOrigemDestino(origemDestino);
        movimentacaoRepository.save(movimentacao);

        return saldo;
    }

    @Transactional
    public SaldoEstoque registrarSaida(
            Long produtoId,
            Long loteId,
            BigDecimal quantidade,
            Long usuarioId,
            String origemDestino) {

        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        SaldoEstoque saldo = saldoRepository
                .findByProdutoIdAndLoteId(produtoId, loteId)
                .orElseThrow(() -> new IllegalArgumentException("Saldo de estoque não encontrado."));

        saldo.baixa(quantidade);
        saldoRepository.save(saldo);

        sincronizarSaldoProduto(produto);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTipo(TipoMovimentacao.SAIDA);
        movimentacao.setProduto(produto);
        movimentacao.setLoteId(loteId);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setUsuarioId(usuarioId);
        movimentacao.setOrigemDestino(origemDestino);
        movimentacaoRepository.save(movimentacao);

        return saldo;
    }

    private void sincronizarSaldoProduto(Produto produto) {
        BigDecimal totalSaldos = saldoRepository.findAll().stream()
                .filter(s -> s.getProduto() != null && produto.getId().equals(s.getProduto().getId()))
                .map(s -> s.getQuantidade() != null ? s.getQuantidade() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        produto.setQuantidade_estoque(totalSaldos.intValue());
        produtoRepository.save(produto);
    }
}
