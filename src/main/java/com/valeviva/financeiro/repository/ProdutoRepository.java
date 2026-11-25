package com.valeviva.financeiro.repository;

import com.valeviva.financeiro.model.Produto;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
=======
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findByNomeAndCodigoContainingIgnoreCase(String nome, String codigo, Pageable pageable);
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
}
