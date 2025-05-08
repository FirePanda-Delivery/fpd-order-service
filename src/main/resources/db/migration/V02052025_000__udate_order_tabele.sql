ALTER TABLE "order"
RENAME COLUMN userid to user_id;

ALTER TABLE "order"
ALTER COLUMN courier_id DROP NOT NULL