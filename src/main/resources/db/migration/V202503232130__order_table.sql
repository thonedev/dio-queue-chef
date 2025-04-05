CREATE TABLE `order` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_number VARCHAR(150),
    items VARCHAR(150),
    create_date DATETIME,
    is_on_hold TINYINT(1),
    stage_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_stage FOREIGN KEY (stage_id)
        REFERENCES stage (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;