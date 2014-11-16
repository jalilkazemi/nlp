CREATE TABLE rss_pages(
    id INTEGER PRIMARY KEY,
    channel_pk INTEGER,
    link VARCHAR(80) UNIQUE NOT NULL
);

CREATE INDEX idx_rss_pages_channel ON rss_pages(channel_pk);
