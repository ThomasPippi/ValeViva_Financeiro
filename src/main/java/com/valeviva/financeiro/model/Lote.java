package com.valeviva.financeiro.model;

import com.valeviva.financeiro.enums.StatusQualidade;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "Lote")
@Table(name = "lote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_producao_id")
    private OrdemProducao ordemOrigem;

    @Column(name = "data_fabricacao", nullable = false)
    private LocalDate dataFabricacao;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_qualidade", nullable = false, length = 30)
    private StatusQualidade statusQualidade = StatusQualidade.QUARENTENA;

    @Column(name = "observacao_qualidade", length = 255)
    private String observacaoQualidade;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public boolean isVencido() {
        return dataValidade != null && dataValidade.isBefore(LocalDate.now());
    }
}