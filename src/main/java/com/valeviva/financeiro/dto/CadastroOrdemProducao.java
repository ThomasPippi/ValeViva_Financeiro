package com.valeviva.financeiro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

<<<<<<< HEAD
=======
import java.time.LocalDate;

>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
public record CadastroOrdemProducao(
        @NotNull(message = "O ID do produto é obrigatório")
        Long produtoId,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
<<<<<<< HEAD
        Integer quantidade
) {}
=======
        Integer quantidade,

        LocalDate prazo,

        Integer prioridade
) {}
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
