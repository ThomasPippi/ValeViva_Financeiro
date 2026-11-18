package com.valeviva.financeiro.repository;

import com.valeviva.financeiro.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
}
