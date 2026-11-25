package com.valeviva.financeiro.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roteiro")
@Getter
@Setter
@NoArgsConstructor
public class Roteiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer versao;

    @Column(nullable = false)
    private boolean ativo;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    // As operações são salvas junto com o roteiro
    @OneToMany(mappedBy = "roteiro", cascade = CascadeType.ALL)
    private List<Operacao> operacoes = new ArrayList<>();

    // Adiciona uma operação e liga ela a este roteiro
    public void adicionarOperacao(Operacao operacao) {
        operacao.setRoteiro(this);
        operacoes.add(operacao);
    }
}