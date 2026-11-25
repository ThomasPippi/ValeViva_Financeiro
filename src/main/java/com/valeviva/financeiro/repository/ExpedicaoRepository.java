
package com.valeviva.financeiro.repository;

// Importa a entidade de expedição
import com.valeviva.financeiro.model.Expedicao;

// Importa o Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;

// Importa listas
import java.util.List;

// Repositório responsável por acessar os registros de expedição
public interface ExpedicaoRepository extends JpaRepository<Expedicao, Long> {

    // Busca todas as expedições de um lote específico
    List<Expedicao> findByLoteId(Long loteId);

    // Busca todas as expedições realizadas por um usuário
    List<Expedicao> findByUsuarioId(Long usuarioId);

    // Busca expedições destinadas a um local específico
    List<Expedicao> findByDestinoContainingIgnoreCase(String destino);
}

