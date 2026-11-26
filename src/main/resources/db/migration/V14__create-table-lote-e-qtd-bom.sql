create table lote(
    id bigint not null auto_increment primary key,
    codigo varchar(100) not null unique,
    produto_id bigint not null,
    ordem_producao_id bigint,
    data_fabricacao date not null,
    data_validade date not null,
    status_qualidade varchar(30) not null default 'QUARENTENA',
    observacao_qualidade varchar(255),
    criado_em datetime not null default current_timestamp,
    atualizado_em datetime not null default current_timestamp on update current_timestamp,

    constraint fk_lote_produto foreign key (produto_id) references produto(id),
    constraint fk_lote_ordem_producao foreign key (ordem_producao_id) references ordem_producao(id)
);

create index idx_lote_validade on lote(data_validade);

-- quantidade de matéria-prima por unidade de produto (usada na confirmação de materiais da OP)
alter table produto_materia_prima add column quantidade_necessaria double not null default 1;
