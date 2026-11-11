package com.valeviva.financeiro.repository;

import com.valeviva.financeiro.model.OrdemProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdemProducaoRepository extends JpaRepository<OrdemProducao, Long> {
}