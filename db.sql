CREATE TABLE IF NOT EXISTS tv_channels (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    slug VARCHAR(255) NOT NULL,
    name VARCHAR(255),
 
    PRIMARY KEY (id),
    INDEX idx_slug_tv_channels(slug)
)
ENGINE=InnoDB COLLATE=utf16_polish_ci;

CREATE TABLE IF NOT EXISTS tv_programs_data (
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	
	name VARCHAR(255) NOT NULL,
	description VARCHAR(3000),
	program_type VARCHAR(255) NOT NULL,
	season INT UNSIGNED DEFAULT NULL,
	episode INT UNSIGNED DEFAULT NULL,
	badge VARCHAR(255) DEFAULT NULL,
	hour_start VARCHAR(5) NOT NULL,
	schedule_date DATE NOT NULL,
	weekday INT UNSIGNED NOT NULL,
	channel_id BIGINT UNSIGNED NOT NULL,

	FOREIGN KEY (channel_id) REFERENCES tv_channels(id) ON UPDATE CASCADE ON DELETE CASCADE,
	
	PRIMARY KEY (id),
	INDEX idx_name_schedule_date_tv_programs_data(name)
)
ENGINE=InnoDB COLLATE=utf16_polish_ci;