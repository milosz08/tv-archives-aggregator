CREATE TABLE IF NOT EXISTS tv_channels (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    slug VARCHAR(255) NOT NULL,
    name VARCHAR(255),
 
    PRIMARY KEY (id),
    INDEX idx_slug_tv_channels(slug)
)
ENGINE=InnoDB COLLATE=utf16_polish_ci;