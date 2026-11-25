create table saldo_estoque(
    id bigint not null auto_increment primary key,
    produto_id bigint not null,
    lote_codigo varchar(100) not null,
    quantidade decimal(15,3) not null,
    versao bigint not null defalt 0,

    foreign key (produto_id)
references produto_(id)
    unique (produto_id, lote codigo)

);