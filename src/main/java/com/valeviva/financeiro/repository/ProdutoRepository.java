package com.valeviva.financeiro.repository;

import com.valeviva.financeiro.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findByNomeAndCodigoContainingIgnoreCase(String nome, String codigo, Pageable pageable);
}
