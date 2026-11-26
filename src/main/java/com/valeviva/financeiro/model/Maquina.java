package com.valeviva.financeiro.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "maquina")
@Getter
@Setter
@NoArgsConstructor
public class Maquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;
}
