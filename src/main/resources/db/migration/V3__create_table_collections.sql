CREATE TABLE collections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    acquired_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,

    CONSTRAINT fk_collections_game_id FOREIGN KEY (game_id) REFERENCES games(id)
);