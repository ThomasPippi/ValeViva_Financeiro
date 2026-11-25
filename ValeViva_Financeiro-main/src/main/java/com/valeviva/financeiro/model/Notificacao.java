package com.valeviva.financeiro.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    private LocalDateTime dataEnvio = LocalDateTime.now();

    private Boolean lida = false;

    public void setLida(Boolean lida) {
        this.lida = lida;
    }
}