<<<<<<< HEAD
create table pedidoCompra(
=======
create table pedido_compra(
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fornecedor_id bigint not null,
    data_pedido date not null,
    data_prometida date not null,
<<<<<<< HEAD
    data_recebimento date not null,
    status varchar(20) not null,
    ordem_producao_id BIGINT,
    CONSTRAINT fk_pedido_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedor (id)
)
=======
    data_recebimento date,
    status varchar(20) not null,
    ordem_producao_id BIGINT,
    CONSTRAINT fk_pedido_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedor (id)
);
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
