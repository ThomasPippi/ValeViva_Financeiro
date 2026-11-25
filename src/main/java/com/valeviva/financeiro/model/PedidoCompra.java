package com.valeviva.financeiro.model;

import com.valeviva.financeiro.enums.StatusPedidoCompra;
<<<<<<< HEAD
=======
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
public class PedidoCompra {
    private Long id;
    private Fornecedor fornecedor;
    private LocalDate data_pedido;
    private LocalDate data_prometida;
    private LocalDate data_recebimento;
    private StatusPedidoCompra status;
    private Long ordem_producao_id;
=======
@Entity(name = "PedidoCompra")
@Table(name = "pedido_compra")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PedidoCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    private LocalDate data_pedido;
    private LocalDate data_prometida;
    private LocalDate data_recebimento;

    @Enumerated(EnumType.STRING)
    private StatusPedidoCompra status;

    private Long ordem_producao_id;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
    private List<ItemPedidoCompra> itens = new ArrayList<>();
}
