package com.valeviva.financeiro.repository;

import com.valeviva.financeiro.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
