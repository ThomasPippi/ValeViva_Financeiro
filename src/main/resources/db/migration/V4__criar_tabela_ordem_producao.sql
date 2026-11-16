CREATE TABLE OrdemProducao (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                produto_id BIGINT NOT NULL,
                                quantidade INT NOT NULL,
                                data_criacao DATETIME NOT NULL,
                                status VARCHAR(50) NOT NULL,
                                CONSTRAINT fk_op_produto FOREIGN KEY (produto_id) REFERENCES produto(id)
);