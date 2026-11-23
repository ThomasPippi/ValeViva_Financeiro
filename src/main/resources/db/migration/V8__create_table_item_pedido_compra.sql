create table item_pedido_compra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_compra_id bigint not null,
    produto_id bigint not null,
    quantidade decimal(15,4) not null,
    preco_unitario decimal(12,2),
    quantidade_recebida decimal(15,4),
    CONSTRAINT fk_item_pedido FOREIGN KEY (pedido_compra_id) REFERENCES pedidoCompra (id),
    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES produto (id)
);
