
package com.valeviva.financeiro.service;

// Importa as entidades utilizadas
import com.valeviva.financeiro.model.Expedicao;
import com.valeviva.financeiro.model.SaldoEstoque;

// Importa os repositórios
import com.valeviva.financeiro.repository.ExpedicaoRepository;
import com.valeviva.financeiro.repository.SaldoEstoqueRepository;

// Importações do Spring
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Trabalha com quantidades decimais
import java.math.BigDecimal;

// Indica que esta classe contém as regras de expedição
@Service
public class ExpedicaoService {

    // Repositórios responsáveis pelo acesso ao banco de dados
    private final ExpedicaoRepository expedicaoRepository;
    private final SaldoEstoqueRepository saldoRepository;

    // Construtor que recebe os repositórios
    public ExpedicaoService(
            ExpedicaoRepository expedicaoRepository,
            SaldoEstoqueRepository saldoRepository) {

        this.expedicaoRepository = expedicaoRepository;
        this.saldoRepository = saldoRepository;
    }

    // Registra uma nova expedição de produtos
    @Transactional
    public Expedicao registrarExpedicao(
            Long produtoId,
            Long loteId,
            BigDecimal quantidade,
            String destino,
            Long usuarioId) {

        // Verifica se o destino foi informado
        if (destino == null || destino.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O destino da expedição deve ser informado."
            );
        }

        // Procura o saldo do produto no lote informado
        SaldoEstoque saldo = saldoRepository
                .findByProdutoIdAndLoteId(produtoId, loteId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Saldo de estoque não encontrado."
                        ));

        // Realiza a baixa da quantidade expedida
        // Impede que seja enviada uma quantidade maior que o estoque disponível
        saldo.baixa(quantidade);

        // Salva o saldo atualizado
        saldoRepository.save(saldo);

        // Cria o registro da expedição
        Expedicao expedicao = new Expedicao();

        expedicao.setLoteId(loteId);
        expedicao.setQuantidade(quantidade);
        expedicao.setDestino(destino);
        expedicao.setUsuarioId(usuarioId);

        // Salva a expedição no banco de dados
        return expedicaoRepository.save(expedicao);
    }
}

