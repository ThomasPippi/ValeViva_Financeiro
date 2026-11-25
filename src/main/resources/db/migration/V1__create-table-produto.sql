<<<<<<< HEAD
    create table produto(
        id bigint not null auto_increment primary key,
        nome varchar(100) not null,
        codigo varchar(30) not null ,
        descricao varchar(250) not null,
        tipo varchar(20) not null,
        unidade varchar(20) not null,
        preco decimal(12,2) not null,
        quantidade_estoque int not null,
        estoque_minimo int not null,
        registro_anvisa varchar(30),
        vencimento_anvisa date,
        is_ativo boolean not null default true
    )
=======
create table produto(
    id bigint not null auto_increment primary key,
    nome varchar(100) not null,
    codigo varchar(30) not null ,
    descricao varchar(250) not null,
    tipo varchar(20) not null,
    unidade varchar(20) not null,
    preco decimal(12,2) not null,
    quantidade_estoque int not null,
    estoque_minimo int not null,
    registro_anvisa varchar(30),
    vencimento_anvisa date,
    is_ativo boolean not null default true
)
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
