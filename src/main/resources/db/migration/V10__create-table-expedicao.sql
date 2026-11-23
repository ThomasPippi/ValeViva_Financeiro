create table expedicao(
    id bigint not null auto_increment primary key,
    lote_id bigint not null,
    quantidade decimal(15,4) not null,
    destino varchar(150) not null,
    data_hora datetime not null default current_timestamp,
    usuario_id bigint not null
);
