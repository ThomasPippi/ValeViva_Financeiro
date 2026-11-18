create table fornecedor(
    id bigint not null auto_increment primary key,
    razao_social varchar(150) not null,
    nome_fantasia varchar(150),
    cnpj char(14) not null unique,
    email varchar(150),
    telefone varchar(20),
    contato varchar(100),
    prazo_entrega_dias int,
    ativo boolean not null default true
)