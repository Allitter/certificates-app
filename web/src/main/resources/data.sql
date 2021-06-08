INSERT INTO certificate (id, name, description, price, duration, create_date, last_update_date) VALUES (1, 'first certificate', 'detailed description for first certificate', 200, 365, '2021-01-21', '2021-02-21');
INSERT INTO certificate (id, name, description, price, duration, create_date, last_update_date) VALUES (5, 'first certificate', 'detailed description for first certificate', 400, 730, '2021-02-21', '2021-08-21');
INSERT INTO certificate (id, name, description, price, duration, create_date, last_update_date) VALUES (2, 'second certificate', 'detailed description for second certificate', 150, 365, '2021-02-21', '2021-03-21');
INSERT INTO certificate (id, name, description, price, duration, create_date, last_update_date) VALUES (3, 'third certificate', 'detailed description for third certificate', 80, 365, '2021-01-21', '2021-02-21');
INSERT INTO certificate (id, name, description, price, duration, create_date, last_update_date) VALUES (4, 'fourth certificate', 'detailed description for fourth certificate',  200, 730, '2020-12-21', '2020-12-31');

INSERT INTO tag (id, name)  VALUES (1, 'first tag');
INSERT INTO tag (id, name)  VALUES (2, 'second tag');
INSERT INTO tag (id, name)  VALUES (3, 'third tag');
INSERT INTO tag (id, name)  VALUES (4, 'fourth tag');

INSERT INTO certificate_tag (id_certificate, id_tag) VALUES (1, 1);
INSERT INTO certificate_tag (id_certificate, id_tag) VALUES (1, 3);
INSERT INTO certificate_tag (id_certificate, id_tag) VALUES (1, 4);
INSERT INTO certificate_tag (id_certificate, id_tag) VALUES (2, 2);
INSERT INTO certificate_tag (id_certificate, id_tag) VALUES (3, 2);
INSERT INTO certificate_tag (id_certificate, id_tag) VALUES (3, 3);
INSERT INTO certificate_tag (id_certificate, id_tag) VALUES (4, 1);
INSERT INTO certificate_tag (id_certificate, id_tag) VALUES (4, 3);
INSERT INTO certificate_tag (id_certificate, id_tag) VALUES (5, 1);
INSERT INTO certificate_tag (id_certificate, id_tag) VALUES (5, 3);
INSERT INTO certificate_tag (id_certificate, id_tag) VALUES (5, 4);

INSERT INTO users (id, login, password) VALUES (1, 'den', '1111');
INSERT INTO users (id, login, password) VALUES (2, 'oleg', '1111');
INSERT INTO users (id, login, password) VALUES (3, 'vlados', '1111');

INSERT INTO purchase (id, id_user, cost) VALUES (1, 1, 600);
INSERT INTO purchase (id, id_user, cost) VALUES (2, 1, 80);
INSERT INTO purchase (id, id_user, cost) VALUES (3, 2, 150);

INSERT INTO purchase_certificate (id_purchase, id_certificate, cnt) VALUES (1, 1, 1);
INSERT INTO purchase_certificate (id_purchase, id_certificate, cnt) VALUES (1, 2, 1);
INSERT INTO purchase_certificate (id_purchase, id_certificate, cnt) VALUES (2, 4, 1);
INSERT INTO purchase_certificate (id_purchase, id_certificate, cnt) VALUES (3, 3, 1);


