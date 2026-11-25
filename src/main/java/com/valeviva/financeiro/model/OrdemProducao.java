package com.valeviva.financeiro.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "OrdemProducao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class OrdemProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false, length = 50)
    private String status;

    private LocalDate prazo;

    @Column(nullable = false)
    private Integer prioridade = 1;

    public OrdemProducao(Produto produto, Integer quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.dataCriacao = LocalDateTime.now();
        this.status = "EMITIDA";
        this.prioridade = 1;
    }
}