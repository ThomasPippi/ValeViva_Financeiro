create table saldo_estoque(
    id bigint not null auto_increment primary key,
    produto_id bigint not null,
    lote_id bigint not null,
    quantidade decimal(15,4) not null default 0,
    version bigint not null default 0,

    constraint fk_saldo_produto foreign key (produto_id) references produto(id),
    constraint uk_saldo_produto_lote unique (produto_id, lote_id)
);
