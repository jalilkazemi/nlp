CREATE TABLE channels(
    id INTEGER PRIMARY KEY,
    title VARCHAR(80) NOT NULL,
    link VARCHAR(80) UNIQUE NOT NULL,
    description VARCHAR(500),
    language CHAR(2)    
);

CREATE TABLE items(
    id INTEGER PRIMARY KEY,
    channel_pk INTEGER NOT NULL,
    title VARCHAR(80) NOT NULL,
    link VARCHAR(80) UNIQUE NOT NULL,
    description VARCHAR(500)
);

CREATE INDEX idx_items_channel ON items(channel_pk);

CREATE TABLE fetchs(
    id INTEGER PRIMARY KEY,
    item_pk INTEGER NOT NULL,
    body TEXT NOT NULL,
    fetched_datetime DATETIME NOT NULL
);

CREATE INDEX idx_fetchs_item_datetime ON fetchs(item_pk, fetched_datetime);
