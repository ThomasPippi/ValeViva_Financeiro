package com.valeviva.financeiro.repository;

import com.valeviva.financeiro.model.MateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // interface é um componente de repositório gerenciado pelo container
public interface MateriaPrimaRepository extends JpaRepository<MateriaPrima, Long> {
    // O JpaRepository<MateriaPrima, Long> recebe:
    // 1. A Entidade que ele vai gerenciar (MateriaPrima)
    // 2. O tipo de dado da chave primária (@Id), que é Long
}
