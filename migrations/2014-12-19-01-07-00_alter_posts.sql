DROP INDEX idx_posts_item_datetime;
CREATE UNIQUE INDEX idx_posts_item ON posts(item_pk);
