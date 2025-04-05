CREATE TABLE stage (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    position TINYINT NOT NULL,
    preparation_queue_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_preparation_queue FOREIGN KEY (preparation_queue_id)
        REFERENCES preparation_queue (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;