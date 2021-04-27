DROP TABLE IF EXISTS certificate_tag;
DROP TABLE IF EXISTS purchase_certificate;
DROP TABLE IF EXISTS purchase;
DROP TABLE IF EXISTS certificate;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS users;

create TABLE certificate
(
    id               INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name             VARCHAR(255),
    description      VARCHAR(255),
    price            INT,
    duration         INT,
    create_date      DATE,
    last_update_date DATE,
    removed          bool DEFAULT false
);

create TABLE tag
(
    id   INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(255)       NOT NULL UNIQUE
);

create TABLE certificate_tag
(
    id_tag         INT NOT NULL,
    id_certificate INT NOT NULL,

    CONSTRAINT pk_certificate_tag PRIMARY KEY (id_tag, id_certificate),

    FOREIGN KEY (id_tag) REFERENCES tag (id) ON DELETE CASCADE,
    FOREIGN KEY (id_certificate) REFERENCES certificate (id) ON DELETE CASCADE
);


create TABLE users
(
    id       INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    login    VARCHAR(255)       NOT NULL UNIQUE,
    password VARCHAR(255)       NOT NULL,
    removed          bool DEFAULT false
);

create TABLE purchase
(
    id      INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    id_user INT NOT NULL,
    create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cost INT DEFAULT 0,
    removed          bool DEFAULT false,

    FOREIGN KEY (id_user) REFERENCES users (id) ON DELETE CASCADE
);

create TABLE purchase_certificate
(
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    id_purchase       INT NOT NULL,
    id_certificate INT NOT NULL,
    cnt INT NOT NULL,

    FOREIGN KEY (id_purchase) REFERENCES purchase (id) ON DELETE CASCADE,
    FOREIGN KEY (id_certificate) REFERENCES certificate (id) ON DELETE CASCADE
)
