ALTER TABLE comment ADD COLUMN log_id UUID;

UPDATE comment SET log_id = reply_to;

ALTER TABLE comment ALTER COLUMN log_id SET NOT NULL;

ALTER TABLE comment DROP COLUMN reply_to;

ALTER TABLE comment ADD COLUMN reply_to UUID;

