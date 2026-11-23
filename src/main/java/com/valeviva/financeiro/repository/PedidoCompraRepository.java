package com.valeviva.financeiro.repository;

import com.valeviva.financeiro.model.PedidoCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {
}
