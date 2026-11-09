package com.valeviva.financeiro.model;

import com.valeviva.financeiro.dto.CadastroMateriaPrima;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "materia_prima")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MateriaPrima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private double custoUnitario;

    @Column(name = "quantidade_estoque")
    private double quantidadeEstoque;

    private String unidadeMedida;

    // ==========================================
    // COLOQUE O CONSTRUTOR AQUI:
    // ==========================================
    public MateriaPrima(CadastroMateriaPrima dto) {
        this.nome = dto.nome();
        this.descricao = dto.descricao();
        this.custoUnitario = dto.custoUnitario();
        this.quantidadeEstoque = dto.quantidadeEstoque();
        this.unidadeMedida = dto.unidadeMedida();
    }
    // ==========================================
}