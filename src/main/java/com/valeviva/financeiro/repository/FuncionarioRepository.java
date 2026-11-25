package com.valeviva.financeiro.repository;

import com.valeviva.financeiro.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
