DROP TABLE IF EXISTS cart_event;

CREATE TABLE cart_event
(
    id           BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    cart_id      VARCHAR(255) NOT NULL,
    payload      VARCHAR(255) NOT NULL,
    created_time DATETIME     NOT NULL,
    event_type   VARCHAR(255) NOT NULL
);

