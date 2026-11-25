CREATE TABLE lote (
                      id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      codigo VARCHAR(100) NOT NULL UNIQUE,
                      produto_id BIGINT NOT NULL,
                      ordem_producao_id BIGINT NULL,
                      data_fabricacao DATE NOT NULL,
                      data_validade DATE NOT NULL,
                      status_qualidade VARCHAR(30) NOT NULL DEFAULT 'QUARENTENA',
                      observacao_qualidade VARCHAR(255) NULL,
                      criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      atualizado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                      CONSTRAINT fk_lote_produto FOREIGN KEY (produto_id) REFERENCES produto(id),
                      CONSTRAINT fk_lote_ordem_producao FOREIGN KEY (ordem_producao_id) REFERENCES ordem_producao(id)
);

CREATE INDEX idx_lote_produto ON lote(produto_id);
CREATE INDEX idx_lote_validade ON lote(data_validade);