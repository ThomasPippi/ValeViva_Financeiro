create table movimentacao_estoque(
    id bigint not null auto_increment primary key,
    tipo varchar(10) not null,
    produto_id bigint not null,
    lote_id bigint not null,
    quantidade decimal(15,4) not null,
    data_hora datetime not null default current_timestamp,
    usuario_id bigint not null,
    origem_destino varchar(150),
    expedicao_id bigint,

    constraint fk_movimentacao_produto foreign key (produto_id) references produto(id),
    constraint fk_movimentacao_expedicao foreign key (expedicao_id) references expedicao(id)
);
