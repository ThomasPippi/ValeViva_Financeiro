ALTER TABLE produto_materia_prima
    ADD COLUMN quantidade_necessaria DOUBLE NOT NULL DEFAULT 1;

ALTER TABLE OrdemProducao
    ADD COLUMN prazo DATE NULL,
    ADD COLUMN prioridade INT NOT NULL DEFAULT 1;