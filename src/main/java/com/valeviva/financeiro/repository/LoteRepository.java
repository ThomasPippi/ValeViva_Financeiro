package com.valeviva.financeiro.repository;

import com.valeviva.financeiro.model.Lote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoteRepository extends JpaRepository<Lote, Long> {
    Optional<Lote> findByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCase(String codigo);
}