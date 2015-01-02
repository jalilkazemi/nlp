CREATE TABLE delimiters(
    id INTEGER PRIMARY KEY,
    symbol CHAR(1) UNIQUE NOT NULL,
    is_period BOOLEAN NOT NULL
);
