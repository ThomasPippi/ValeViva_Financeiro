create table pedidoCompra(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fornecedor_id bigint not null,
    data_pedido date not null,
    data_prometida date not null,
    data_recebimento date not null,
    status varchar(20) not null,
    ordem_producao_id BIGINT,
    CONSTRAINT fk_pedido_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedor (id)
);
