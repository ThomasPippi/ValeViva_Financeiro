package com.valeviva.financeiro.model;

import com.valeviva.financeiro.dto.CadastroFornecedor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Fornecedor")
@Table(name = "fornecedor")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String razao_social;
    private String nome_fantasia;
    private String cnpj;
    private String email;
    private String telefone;
    private int prazo_entrega_dias;
    private boolean is_ativo = true;

    public Fornecedor(CadastroFornecedor fornecedor) {
        this.razao_social = fornecedor.razao_social();
        this.nome_fantasia = fornecedor.nome_fantasia();
<<<<<<< HEAD
        this.cnpj = fornecedor.cnpj();
=======
        // guarda só os números do CNPJ (a coluna no banco é char(14))
        this.cnpj = fornecedor.cnpj().replaceAll("[^0-9]", "");
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
        this.email = fornecedor.email();
        this.telefone = fornecedor.telefone();
        this.prazo_entrega_dias = fornecedor.prazo_entrega_dias();
        this.is_ativo = fornecedor.is_ativo();
    }
}
