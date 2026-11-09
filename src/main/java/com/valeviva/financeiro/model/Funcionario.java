package com.valeviva.financeiro.model;

import com.valeviva.financeiro.dto.CadastroFuncionario;
import com.valeviva.financeiro.enums.Setor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Funcionario")
@Table(name = "funcionario")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String matricula;
    private String cargo;
    @Enumerated(EnumType.STRING)
    private Setor setor;
    private boolean is_ativo;

    public Funcionario (CadastroFuncionario funcionario) {
        this.nome = funcionario.nome();
        this.matricula = funcionario.matricula();
        this.cargo = funcionario.cargo();
        this.setor = funcionario.setor();
        this.is_ativo = funcionario.is_ativo();
    }
}
