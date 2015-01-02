CREATE TABLE delimiters(
    id INTEGER PRIMARY KEY,
    symbol CHAR(1) UNIQUE NOT NULL,
    is_period BOOLEAN NOT NULL
);

CREATE TABLE sentence_count(
    count INTEGER NOT NULL;
);

INSERT INTO sentence_count(count) VALUES(0);

CREATE TABLE words(
    id INTEGER PRIMARY KEY,
    word VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE unigrams(
    id INTEGER PRIMARY KEY,
    word_pk INTEGER NOT NULL,
    count INTEGER NOT NULL
);

CREATE TABLE bigrams(
    id INTEGER PRIMARY KEY,
    word1_pk INTEGER NOT NULL,
    word2_pk INTEGER NOT NULL,
    count INTEGER NOT NULL
);

CREATE TABLE trigrams(
    id INTEGER PRIMARY KEY,
    word1_pk INTEGER NOT NULL,
    word2_pk INTEGER NOT NULL,
    word3_pk INTEGER NOT NULL,
    count INTEGER NOT NULL
);

CREATE UNIQUE INDEX idx_unigrams_word ON unigrams(word_pk);
CREATE UNIQUE INDEX idx_bigrams_word ON bigrams(word1_pk, word2_pk);
CREATE UNIQUE INDEX idx_trigrams_word ON trigrams(word1_pk, word2_pk, word3_pk);

CREATE TABLE parsed_posts(
    id INTEGER PRIMARY KEY,
    parsed_post_pk INTEGER UNIQUE NOT NULL
);
