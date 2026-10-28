package com.valeviva.financeiro.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Produto {


    private Long id;
    private String nome;
    private String descricao;
    private double preço;
    private int quantidadeEstoque;
    private String categoria;




}
