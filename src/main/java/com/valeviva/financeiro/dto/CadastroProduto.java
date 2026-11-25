package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.enums.TipoProduto;

<<<<<<< HEAD
=======
import java.math.BigDecimal;
import java.time.LocalDate;

>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
public record CadastroProduto(
     String nome,
     String codigo,
     String descricao,
     TipoProduto tipo,
     String unidade,
<<<<<<< HEAD
     double preco,
     int quantidade_estoque,
     int estoque_minimo,
     String registro_anvisa,
     String vencimento_anvisa,
=======
     BigDecimal preco,
     int quantidade_estoque,
     int estoque_minimo,
     String registro_anvisa,
     LocalDate vencimento_anvisa,
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
     boolean is_ativo
) {}
