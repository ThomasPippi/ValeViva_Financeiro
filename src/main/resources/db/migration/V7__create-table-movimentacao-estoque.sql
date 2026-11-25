create table movimentacao_estoque(
    id bigint not null auto_increment primary key,
    produto_id bigint not null,
    lote_codigo varchar(100) not null,
    tipo varchar(20) not null,
    quantidade decimal(15,3) not null,
    data_hora datetime not null default current_timestamp,
    usuario varchar(100) not null,

    foreign key(produto_id) references produto_(id)
);