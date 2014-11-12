ALTER TABLE fetchs ADD COLUMN meta TEXT;
ALTER TABLE fetchs RENAME TO posts;
DROP INDEX idx_fetchs_item_datetime;
CREATE UNIQUE INDEX idx_posts_item_datetime ON posts(item_pk, fetched_datetime);
