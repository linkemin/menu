create table goods_type
(
    id         int auto_increment
        primary key,
    name       varchar(255) null,
    show_order varchar(255) null
);

INSERT INTO menu.goods_type (id, name, show_order) VALUES (1, '三文治', '1');
INSERT INTO menu.goods_type (id, name, show_order) VALUES (2, '台湾手卷饭团', '2');
INSERT INTO menu.goods_type (id, name, show_order) VALUES (3, '饭团', '3');
INSERT INTO menu.goods_type (id, name, show_order) VALUES (4, '豆浆', '4');
INSERT INTO menu.goods_type (id, name, show_order) VALUES (5, '汉堡', '5');