package com.valeviva.financeiro.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
<<<<<<< HEAD
@Table(name = "OrdemProducao")
=======
@Table(name = "ordem_producao")
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class OrdemProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false, length = 50)
=======
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    private Integer quantidade;

    private LocalDateTime dataCriacao;

>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
    private String status;

    private LocalDate prazo;

<<<<<<< HEAD
    @Column(nullable = false)
    private Integer prioridade = 1;
=======
    private Integer prioridade;


>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb

    public OrdemProducao(Produto produto, Integer quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.dataCriacao = LocalDateTime.now();
        this.status = "EMITIDA";
<<<<<<< HEAD
        this.prioridade = 1;
=======
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
    }
}