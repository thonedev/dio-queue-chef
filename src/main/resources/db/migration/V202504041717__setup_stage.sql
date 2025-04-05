INSERT INTO queuechef_db.preparation_queue (name)
SELECT 'Main'
WHERE NOT EXISTS (
    SELECT 1 FROM queuechef_db.preparation_queue WHERE name = 'Main'
);

INSERT INTO queuechef_db.stage (name, stage_order, preparation_queue_id)
SELECT 'Recebimento', 1, 1
WHERE NOT EXISTS (
    SELECT 1 FROM queuechef_db.stage WHERE name = 'Recebimento'
);

INSERT INTO queuechef_db.stage (name, stage_order, preparation_queue_id)
SELECT 'Cancelado', 4, 1
WHERE NOT EXISTS (
    SELECT 1 FROM queuechef_db.stage WHERE name = 'Cancelado'
);

INSERT INTO queuechef_db.stage (name, stage_order, preparation_queue_id)
SELECT 'Concluído', 3, 1
WHERE NOT EXISTS (
    SELECT 1 FROM queuechef_db.stage WHERE name = 'Concluído'
);

INSERT INTO queuechef_db.stage (name, stage_order, preparation_queue_id)
SELECT 'Em Progresso', 2, 1
WHERE NOT EXISTS (
    SELECT 1 FROM queuechef_db.stage WHERE name = 'Em Progresso'
);