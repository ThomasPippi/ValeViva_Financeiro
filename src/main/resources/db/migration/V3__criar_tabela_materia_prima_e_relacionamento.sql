
CREATE TABLE materia_prima (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               nome VARCHAR(255) NOT NULL,
                               descricao TEXT,
                               custo_unitario DOUBLE NOT NULL,
                               quantidade_estoque DOUBLE NOT NULL,
                               unidade_medida VARCHAR(20) NOT NULL
);

-- Cria a tabela de relacionamento entre Produto e Matéria-Prima
CREATE TABLE produto_materia_prima (
                                       produto_id BIGINT NOT NULL,
                                       materia_prima_id BIGINT NOT NULL,
                                       PRIMARY KEY (produto_id, materia_prima_id),
                                       CONSTRAINT fk_produto FOREIGN KEY (produto_id) REFERENCES produto(id),
                                       CONSTRAINT fk_materia_prima FOREIGN KEY (materia_prima_id) REFERENCES materia_prima(id)
);