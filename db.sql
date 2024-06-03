CREATE TABLE IF NOT EXISTS weekdays (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    name VARCHAR(50) NOT NULL,
    alias VARCHAR(5) NOT NULL,
 
    PRIMARY KEY (id)
)
ENGINE=InnoDB COLLATE=utf16_polish_ci;

CREATE TABLE IF NOT EXISTS tv_channels (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    slug VARCHAR(255) NOT NULL,
    name VARCHAR(255),
 
    PRIMARY KEY (id),
    INDEX (slug)
)
ENGINE=InnoDB COLLATE=utf16_polish_ci;

INSERT INTO weekdays (id,name,alias) VALUES
(1,"poniedziałek", "pn"),
(2,"wtorek", "wt"),
(3,"środa", "śr"),
(4,"czwartek", "czw"),
(5,"piątek", "pt"),
(6,"sobota", "sb"),
(7,"niedziela", "nd");