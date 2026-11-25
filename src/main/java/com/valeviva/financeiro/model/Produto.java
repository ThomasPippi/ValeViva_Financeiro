package com.valeviva.financeiro.model;

import com.valeviva.financeiro.dto.CadastroProduto;
import com.valeviva.financeiro.enums.TipoProduto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

<<<<<<< HEAD
=======
import java.math.BigDecimal;
import java.time.LocalDate;
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
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
    private String codigo;
    private String descricao;
<<<<<<< HEAD
    private TipoProduto tipo;
    private String unidade; //UN, KG, TON
    private double preco;
    private int quantidade_estoque;
    private int estoque_minimo;
    private String registro_anvisa;
    private String vencimento_anvisa;
=======
    @Enumerated(EnumType.STRING)
    private TipoProduto tipo;
    private String unidade; //UN, KG, TON
    @Column(precision = 12, scale = 2)
    private BigDecimal preco;
    private int quantidade_estoque;
    private int estoque_minimo;
    private String registro_anvisa;
    private LocalDate vencimento_anvisa;
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
    private boolean is_ativo;


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
        this.codigo = produto.codigo();
        this.descricao = produto.descricao();
        this.tipo = produto.tipo();
        this.unidade = produto.unidade();
        this.preco = produto.preco();
        this.quantidade_estoque = produto.quantidade_estoque();
        this.estoque_minimo = produto.estoque_minimo();
        this.registro_anvisa = produto.registro_anvisa();
        this.vencimento_anvisa = produto.vencimento_anvisa();
        this.is_ativo = produto.is_ativo();
    }
}