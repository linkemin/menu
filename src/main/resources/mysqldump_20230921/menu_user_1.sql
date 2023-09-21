create table user
(
    id           int auto_increment
        primary key,
    user_code    varchar(255) null,
    user_name    varchar(255) null,
    user_comment varchar(255) null,
    mail         varchar(255) null,
    mobile       varchar(20)  null,
    sex          varchar(1)   null comment '0-男   1-女',
    user_type    varchar(1)   null comment '0-邮校  1-大观南',
    favorite     varchar(255) null comment '喜好'
);

INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (1, '007', '许旭楷', '', null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (2, '009', '王正宇', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (3, '017', '钟海萍', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (4, '004', '朱亚文', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (5, '013', '刘自豪', '', null, '9999', null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (6, '005', '梁飞飞', '', null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (7, '010', '黄立健', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (8, '012', '邹锐杭', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (9, '003', '梁晓欣', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (10, '002', '王万', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (11, '001', '李燕', '', '', null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (12, '015', '欧涛涛', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (13, '008', '汪洋', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (14, '006', '杨棋添', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (15, '011', '黄绍朋', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (16, '014', '汪越', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (17, '016', '测试用', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (18, '018', '陈浩帆', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (19, '019', '黄钰欣', null, null, null, null, '0', null);
INSERT INTO menu.user (id, user_code, user_name, user_comment, mail, mobile, sex, user_type, favorite) VALUES (20, '020', '林克敏', null, null, null, null, '0', null);