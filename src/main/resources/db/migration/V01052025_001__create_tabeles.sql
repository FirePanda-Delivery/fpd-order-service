CREATE SEQUENCE IF NOT EXISTS order_id_sequence START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS order_product_id_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE "order"
(
    id                 BIGINT                      NOT NULL,
    restaurant_id      BIGINT                      NOT NULL,
    userid             BIGINT                      NOT NULL,
    date               TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status             VARCHAR(255)                NOT NULL,
    courier_id         BIGINT                      NOT NULL,
    time_start         time WITHOUT TIME ZONE      NOT NULL,
    time_end           time WITHOUT TIME ZONE,
    address            VARCHAR(255)                NOT NULL,
    city               VARCHAR(255),
    restaurant_address VARCHAR(255),
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE order_product
(
    id         BIGINT  NOT NULL,
    count      INTEGER NOT NULL,
    product_id BIGINT  NOT NULL,
    order_id   BIGINT  NOT NULL,
    CONSTRAINT pk_orderproduct PRIMARY KEY (id)
);

ALTER TABLE order_product
    ADD CONSTRAINT FK_ORDERPRODUCT_ON_ORDER FOREIGN KEY (order_id) REFERENCES "order" (id);