create table produto(
    id bigint not null auto_increment primary key,
    nome varchar(100) not null,
    descricao varchar(255) not null,
    preco decimal(10,2) not null,
    quantidadeEstoque int not null,
    categoria varchar(255) not null,
)