create table maquina(
    id bigint not null auto_increment primary key,
    nome varchar(100) not null
);

create table item_bom(
    id bigint not null auto_increment primary key,
    produto_id bigint not null,
    material_id bigint not null,
    quantidade_por_unidade decimal(15,4) not null,
    versao int not null,
    ativa boolean not null default true,
    criado_em datetime not null default current_timestamp,

    constraint fk_item_bom_produto foreign key (produto_id) references produto(id),
    constraint fk_item_bom_material foreign key (material_id) references produto(id)
);

create table roteiro(
    id bigint not null auto_increment primary key,
    produto_id bigint not null,
    versao int not null,
    ativo boolean not null default true,
    criado_em datetime not null default current_timestamp,

    constraint fk_roteiro_produto foreign key (produto_id) references produto(id)
);

create table operacao(
    id bigint not null auto_increment primary key,
    roteiro_id bigint not null,
    sequencia int not null,
    descricao varchar(150) not null,
    maquina_id bigint,
    tempo_padrao_minutos decimal(10,2) not null,

    constraint fk_operacao_roteiro foreign key (roteiro_id) references roteiro(id),
    constraint fk_operacao_maquina foreign key (maquina_id) references maquina(id)
);

create table item_necessidade(
    id bigint not null auto_increment primary key,
    ordem_producao_id bigint not null,
    material_id bigint not null,
    necessario decimal(15,4) not null,
    disponivel decimal(15,4) not null,
    falta decimal(15,4) not null,

    constraint fk_item_necessidade_ordem foreign key (ordem_producao_id) references ordem_producao(id),
    constraint fk_item_necessidade_material foreign key (material_id) references produto(id)
);

create table cartao_kanban(
    id bigint not null auto_increment primary key,
    ordem_producao_id bigint not null,
    etapa varchar(30) not null,
    criado_em datetime not null default current_timestamp,

    constraint fk_cartao_kanban_ordem foreign key (ordem_producao_id) references ordem_producao(id)
);
