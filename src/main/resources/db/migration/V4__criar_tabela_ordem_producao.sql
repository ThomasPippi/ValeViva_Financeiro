<<<<<<< HEAD
CREATE TABLE OrdemProducao (
=======
CREATE TABLE ordem_producao (
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    data_criacao DATETIME NOT NULL,
<<<<<<< HEAD
    status VARCHAR(50) NOT NULL,
=======
    status VARCHAR(50),
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
    CONSTRAINT fk_op_produto FOREIGN KEY (produto_id) REFERENCES produto(id)
);