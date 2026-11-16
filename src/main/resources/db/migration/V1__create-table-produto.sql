create table produto(
    id bigint not null auto_increment primary key,
    nome varchar(100) not null,
    codigo not null varchar(30) not null unique,
    descricao varchar(250) not null,
    tipo varchar(20) not null,
    unidade varchar(20) not null,
    preco decimal(10,2) not null,
    quantidade_estoque int not null,
    estoque_minimo int not null,
    registro_anvisa varchar(30),
    vencimento_anvisa date,
    is_ativo boolean
)