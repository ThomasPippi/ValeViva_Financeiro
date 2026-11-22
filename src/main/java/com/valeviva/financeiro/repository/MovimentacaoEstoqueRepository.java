package com.valeviva.financeiro.repository;

// Importa a entidade de movimentação de estoque
import com.valeviva.financeiro.model.MovimentacaoEstoque;

// Importa o Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;

// Importa listas para retornar vários registros
import java.util.List;

// Repositório responsável pelo histórico de movimentações
public interface MovimentacaoEstoqueRepository
        extends JpaRepository<MovimentacaoEstoque, Long> {

    // Busca todas as movimentações de um produto
    List<MovimentacaoEstoque> findByProdutoId(Long produtoId);

    // Busca todas as movimentações de um lote
    List<MovimentacaoEstoque> findByLoteId(Long loteId);

    // Busca todas as movimentações de um determinado tipo
    List<MovimentacaoEstoque> findByTipo(
            com.valeviva.financeiro.enums.TipoMovimentacao tipo
    );
}
