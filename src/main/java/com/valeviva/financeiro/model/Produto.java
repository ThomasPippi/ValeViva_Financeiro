package com.valeviva.financeiro.model;

import com.valeviva.financeiro.dto.CadastroProduto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Produto")
@Table(name = "produto")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private double preco;
    private int quantidade_estoque;
    private String categoria;


    @ManyToMany
    @JoinTable(
            name = "produto_materia_prima",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "materia_prima_id")
    )
    private List<MateriaPrima> materiasPrimas = new ArrayList<>();
    // ==========================================

    public Produto(CadastroProduto produto){
        this.nome = produto.nome();
        this.descricao = produto.descricao();
        this.preco = produto.preco();
        this.quantidade_estoque = produto.quantidade_estoque();
        this.categoria = produto.categoria();
    }
}