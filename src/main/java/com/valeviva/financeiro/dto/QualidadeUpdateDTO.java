package com.valeviva.financeiro.dto;

import com.valeviva.financeiro.enums.StatusQualidade;

public record QualidadeUpdateDTO(
        StatusQualidade statusQualidade,
        String observacao
) {}
