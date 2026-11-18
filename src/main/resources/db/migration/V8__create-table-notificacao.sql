create table notificacao(
    id bigint not null auto increment primary key,
    mensagem varchar(255) not null,
    tipo varchar(50) not null,
    lida boolean not null default false,
    data_criacao datetime not null default current_timestamp
);