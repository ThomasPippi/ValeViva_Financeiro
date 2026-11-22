
package com.valeviva.financeiro.model;

// Importações para trabalhar com o banco de dados
import jakarta.persistence.*;

// Bibliotecas para gerar getters, setters e construtores
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Tipos de dados utilizados
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Indica que esta classe representa uma tabela no banco
@Entity
@Table(name = "expedicao")

// Gera automaticamente os construtores
@NoArgsConstructor
@AllArgsConstructor

// Gera automaticamente os métodos de acesso
@Getter
@Setter
public class Expedicao {

    // Identificador único da expedição
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificador do lote que será expedido
    @Column(name = "lote_id", nullable = false)
    private Long loteId;

    // Quantidade de produtos enviados
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal quantidade;

    // Local ou empresa que receberá os produtos
    @Column(nullable = false, length = 150)
    private String destino;

    // Data e horário em que a expedição aconteceu
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    // Identificador do usuário responsável pela expedição
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    // Executa automaticamente antes de salvar no banco
    @PrePersist
    public void registrarData() {

        // Se nenhuma data foi informada, utiliza a data e hora atuais
        if (dataHora == null) {
            dataHora = LocalDateTime.now();
        }
    }
}
