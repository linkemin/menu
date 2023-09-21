create table goods
(
    id          int auto_increment
        primary key,
    goods_code  varchar(255)   null,
    goods_name  varchar(255)   null,
    show_order  varchar(255)   null,
    type_id     int            null,
    goods_price decimal(10, 2) null,
    show_flag   varchar(1)     null,
    constraint type_goods
        foreign key (type_id) references goods_type (id)
);

INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (101, '1_1', '火腿', '1', 1, 5.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (102, '1_2', '紫米', '2', 1, 5.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (103, '1_3', '烤肠', '3', 1, 6.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (104, '1_4', '香煎鸡扒', '4', 1, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (105, '1_5', '鸡柳', '5', 1, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (106, '1_6', '芋泥麻薯', '6', 1, 6.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (107, '1_7', '芝士培根', '7', 1, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (108, '1_8', '紫薯麻薯', '8', 1, 6.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (109, '1_9', '厚切双拼', '9', 1, 12.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (110, '1_10', '牛肉饼', '10', 1, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (111, '1_11', '芋泥', '11', 1, 6.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (112, '1_12', '紫米鸡扒', '12', 1, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (113, '1_13', '紫薯', '13', 1, 6.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (114, '1_14', '紫米麻薯', '14', 1, 6.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (115, '1_15', '黑椒鸡扒', '15', 1, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (116, '1_16', '奥尔良鸡扒', '16', 1, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (202, '2_2', '鸡扒', '2', 2, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (203, '2_3', '鸡柳', '3', 2, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (204, '2_4', '蟹柳（售罄）', '4', 2, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (205, '2_5', '小酥肉', '5', 2, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (206, '2_6', '盐酥鸡米花（售罄）', '6', 2, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (207, '2_7', '牛肉饼', '7', 2, 7.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (301, '3_1', '火腿', '1', 3, 3.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (302, '3_2', '红豆（售罄）', '2', 3, 3.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (303, '3_3', '咸蛋黄（售罄）', '3', 3, 4.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (304, '3_4', '鸡扒', '4', 3, 4.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (305, '3_5', '鸡柳', '5', 3, 4.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (306, '3_6', '玉米吞拿鱼（售罄）', '6', 3, 4.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (307, '3_7', '牛肉饼', '7', 3, 4.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (401, '4_1', '五谷杂粮豆浆', '1', 4, 2.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (501, '5_1', '黑椒鸡扒堡', '1', 5, 8.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (502, '5_2', '奥尔良鸡扒堡', '2', 5, 8.00, null);
INSERT INTO menu.goods (id, goods_code, goods_name, show_order, type_id, goods_price, show_flag) VALUES (503, '5_3', '芝士肉饼堡', '3', 5, 8.00, null);