
package com.valeviva.financeiro.repository;

// Importa a classe que representa o saldo do estoque
import com.valeviva.financeiro.model.SaldoEstoque;

// Permite trabalhar com identificadores do tipo Long
import org.springframework.data.jpa.repository.JpaRepository;

// Interface responsável pelas operações no banco de dados
public interface SaldoEstoqueRepository extends JpaRepository<SaldoEstoque, Long> {

    // Busca o saldo de estoque de um produto em um lote específico
    java.util.Optional<SaldoEstoque> findByProdutoIdAndLoteId(
            Long produtoId,
            Long loteId
    );
}
