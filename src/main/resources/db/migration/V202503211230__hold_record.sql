CREATE TABLE hold_record(
    id BIGINT NOT NULL auto_increment,
    hold_reason VARCHAR(150) NOT NULL,
    release_reason VARCHAR(150) DEFAULT NULL,
    hold_date DATE DEFAULT NULL,
    release_date DATE DEFAULT NULL,
    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;


-- ALTER TABLE hold_record
-- MODIFY COLUMN release_date DATE DEFAULT NULL,
-- MODIFY COLUMN release_reason VARCHAR(150) DEFAULT NULL;