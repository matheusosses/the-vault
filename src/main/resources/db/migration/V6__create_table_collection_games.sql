CREATE TABLE collection_games (
    collection_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    PRIMARY KEY (collection_id, game_id),
    CONSTRAINT fk_cg_collection FOREIGN KEY (collection_id) REFERENCES collections(id),
    CONSTRAINT fk_cg_game FOREIGN KEY (game_id) REFERENCES games(id)
);