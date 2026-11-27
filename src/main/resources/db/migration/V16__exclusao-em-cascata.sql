-- Permite excluir produtos, fornecedores, pedidos de compra e ordens de produção:
-- os registros dependentes são apagados junto (ON DELETE CASCADE).
-- lote.ordem_producao_id é opcional, então só fica NULL quando a OP é apagada.

alter table pedido_compra drop foreign key fk_pedido_fornecedor;
alter table pedido_compra add constraint fk_pedido_fornecedor foreign key (fornecedor_id) references fornecedor(id) on delete cascade;

alter table item_pedido_compra drop foreign key fk_item_pedido;
alter table item_pedido_compra add constraint fk_item_pedido foreign key (pedido_compra_id) references pedido_compra(id) on delete cascade;

alter table item_pedido_compra drop foreign key fk_item_produto;
alter table item_pedido_compra add constraint fk_item_produto foreign key (produto_id) references produto(id) on delete cascade;

alter table produto_materia_prima drop foreign key fk_produto;
alter table produto_materia_prima add constraint fk_produto foreign key (produto_id) references produto(id) on delete cascade;

alter table item_bom drop foreign key fk_item_bom_produto;
alter table item_bom add constraint fk_item_bom_produto foreign key (produto_id) references produto(id) on delete cascade;

alter table item_bom drop foreign key fk_item_bom_material;
alter table item_bom add constraint fk_item_bom_material foreign key (material_id) references produto(id) on delete cascade;

alter table roteiro drop foreign key fk_roteiro_produto;
alter table roteiro add constraint fk_roteiro_produto foreign key (produto_id) references produto(id) on delete cascade;

alter table operacao drop foreign key fk_operacao_roteiro;
alter table operacao add constraint fk_operacao_roteiro foreign key (roteiro_id) references roteiro(id) on delete cascade;

alter table ordem_producao drop foreign key fk_op_produto;
alter table ordem_producao add constraint fk_op_produto foreign key (produto_id) references produto(id) on delete cascade;

alter table item_necessidade drop foreign key fk_item_necessidade_ordem;
alter table item_necessidade add constraint fk_item_necessidade_ordem foreign key (ordem_producao_id) references ordem_producao(id) on delete cascade;

alter table item_necessidade drop foreign key fk_item_necessidade_material;
alter table item_necessidade add constraint fk_item_necessidade_material foreign key (material_id) references produto(id) on delete cascade;

alter table cartao_kanban drop foreign key fk_cartao_kanban_ordem;
alter table cartao_kanban add constraint fk_cartao_kanban_ordem foreign key (ordem_producao_id) references ordem_producao(id) on delete cascade;

alter table lote drop foreign key fk_lote_produto;
alter table lote add constraint fk_lote_produto foreign key (produto_id) references produto(id) on delete cascade;

alter table lote drop foreign key fk_lote_ordem_producao;
alter table lote add constraint fk_lote_ordem_producao foreign key (ordem_producao_id) references ordem_producao(id) on delete set null;

alter table saldo_estoque drop foreign key fk_saldo_produto;
alter table saldo_estoque add constraint fk_saldo_produto foreign key (produto_id) references produto(id) on delete cascade;

alter table movimentacao_estoque drop foreign key fk_movimentacao_produto;
alter table movimentacao_estoque add constraint fk_movimentacao_produto foreign key (produto_id) references produto(id) on delete cascade;
