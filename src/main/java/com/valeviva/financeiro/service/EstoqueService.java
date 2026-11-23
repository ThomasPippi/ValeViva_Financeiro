
package com.valeviva.financeiro.service;

// Importa as classes responsáveis pelos dados do estoque
import com.valeviva.financeiro.model.Produto;
import com.valeviva.financeiro.model.SaldoEstoque;
import com.valeviva.financeiro.model.MovimentacaoEstoque;

// Importa o tipo de movimentação
import com.valeviva.financeiro.enums.TipoMovimentacao;

// Importa os repositórios
import com.valeviva.financeiro.repository.ProdutoRepository;
import com.valeviva.financeiro.repository.SaldoEstoqueRepository;
import com.valeviva.financeiro.repository.MovimentacaoEstoqueRepository;

// Importações do Spring
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Trabalha com quantidades decimais
import java.math.BigDecimal;

// Indica que esta classe contém as regras de negócio do estoque
@Service
public class EstoqueService {

    // Repositórios utilizados pelo serviço
    private final ProdutoRepository produtoRepository;
    private final SaldoEstoqueRepository saldoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    // Construtor responsável por receber os repositórios
    public EstoqueService(
            ProdutoRepository produtoRepository,
            SaldoEstoqueRepository saldoRepository,
            MovimentacaoEstoqueRepository movimentacaoRepository) {

        this.produtoRepository = produtoRepository;
        this.saldoRepository = saldoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    // Método responsável por registrar uma entrada no estoque
    @Transactional
    public SaldoEstoque registrarEntrada(
            Long produtoId,
            Long loteId,
            BigDecimal quantidade,
            Long usuarioId,
            String origemDestino) {

        // Procura o produto no banco de dados
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Produto não encontrado."));

        // Procura o saldo do produto no lote informado
        // Caso não exista, cria um novo saldo começando em zero
        SaldoEstoque saldo = saldoRepository
                .findByProdutoIdAndLoteId(produtoId, loteId)
                .orElseGet(() -> {
                    SaldoEstoque novoSaldo = new SaldoEstoque();
                    novoSaldo.setProduto(produto);
                    novoSaldo.setLoteId(loteId);
                    novoSaldo.setQuantidade(BigDecimal.ZERO);
                    return novoSaldo;
                });

        // Adiciona a quantidade recebida ao estoque
        saldo.entrada(quantidade);

        // Salva o saldo atualizado
        saldoRepository.save(saldo);

        // Cria o registro da movimentação
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTipo(TipoMovimentacao.ENTRADA);
        movimentacao.setProduto(produto);
        movimentacao.setLoteId(loteId);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setUsuarioId(usuarioId);
        movimentacao.setOrigemDestino(origemDestino);

        // Salva o histórico da entrada
        movimentacaoRepository.save(movimentacao);

        return saldo;
    }

    // Método responsável por registrar uma saída do estoque
    @Transactional
    public SaldoEstoque registrarSaida(
            Long produtoId,
            Long loteId,
            BigDecimal quantidade,
            Long usuarioId,
            String origemDestino) {

        // Procura o produto no banco de dados
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Produto não encontrado."));

        // Procura o saldo do produto no lote informado
        SaldoEstoque saldo = saldoRepository
                .findByProdutoIdAndLoteId(produtoId, loteId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Saldo de estoque não encontrado."));

        // Retira a quantidade solicitada
        // O método baixa impede que o estoque fique negativo
        saldo.baixa(quantidade);

        // Salva o saldo atualizado
        saldoRepository.save(saldo);

        // Cria o registro da movimentação
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTipo(TipoMovimentacao.SAIDA);
        movimentacao.setProduto(produto);
        movimentacao.setLoteId(loteId);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setUsuarioId(usuarioId);
        movimentacao.setOrigemDestino(origemDestino);

        // Salva o histórico da saída
        movimentacaoRepository.save(movimentacao);

        return saldo;
    }
}
