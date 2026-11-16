package com.valeviva.financeiro.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ordem_producao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class OrdemProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    private Integer quantidade;

    private LocalDateTime dataCriacao;



    public OrdemProducao(Produto produto, Integer quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.dataCriacao = LocalDateTime.now();

    }
}