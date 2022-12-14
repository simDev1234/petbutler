INSERT INTO user
    (id, user_role, user_status, email, password,
     butler_level, phone, email_auth_yn, registered_at, updated_at)
VALUES
    (1, 'ROLE_ADMIN', 'IN_USE', 'admin@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     2, '010-1234-5678', true, '2022-10-11', '2022-11-11'),
    (2, 'ROLE_ADMIN', 'IN_USE', 'admin2@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     2, '010-2345-5678', true, '2022-10-11', '2022-11-11'),
    (3, 'ROLE_ADMIN', 'IN_USE', 'admin3@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     2, '010-3456-5678', true, '2022-10-11', '2022-11-11'),
    (4, 'ROLE_REGULAR', 'IN_USE', 'test@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     12, '010-1456-5678', true, '2022-10-11', '2022-11-11'),
    (5, 'ROLE_REGULAR', 'IN_USE', 'test2@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     2, '010-2342-5678', true, '2022-10-11', '2022-11-11'),
    (6, 'ROLE_REGULAR', 'IN_USE', 'test3@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     10, '010-1232-5678', true, '2022-10-11', '2022-11-11'),
    (7, 'ROLE_REGULAR', 'IN_USE', 'test4@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     1, '010-2222-5678', true, '2022-10-11', '2022-11-11'),
    (8, 'ROLE_REGULAR', 'IN_USE', 'test5@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     3, '010-1111-5678', true, '2022-10-11', '2022-11-11'),
    (9, 'ROLE_REGULAR', 'IN_USE', 'test6@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     7, '010-3333-5678', true, '2022-10-11', '2022-11-11'),
    (10, 'ROLE_REGULAR', 'IN_USE', 'test7@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     1, '010-1231-5678', true, '2022-10-11', '2022-11-11'),
    (11, 'ROLE_REGULAR', 'IN_USE', 'test8@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     4, '010-1234-5678', true, '2022-10-11', '2022-11-11'),
    (12, 'ROLE_REGULAR', 'IN_USE', 'test9@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     2, '010-1231-5678', true, '2022-10-11', '2022-11-11'),
    (13, 'ROLE_REGULAR', 'IN_USE', 'test10@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     1, '010-5555-5678', true, '2022-10-11', '2022-11-11')
ON DUPLICATE KEY UPDATE id = VALUES(id)
;

INSERT  INTO pet
(id, user_id, kind, name, thumbnail_url_path, thumbnail_local_path, registered_at, updated_at)
VALUES
    (1, 1, 'dog', '??????1', '/files/2022/11/18/1c7a3ca1cff447b3abd33c5a90ee565c.jpg', '', '2022-11-02', '2022-11-03'),
    (2, 1, 'dog', '??????2', '/files/2022/11/18/1c7a3ca1cff447b3abd33c5a90ee565c.jpg', '', '2022-11-02', '2022-11-03'),
    (3, 1, 'dog', '??????3', '/files/2022/11/18/1c7a3ca1cff447b3abd33c5a90ee565c.jpg', '', '2022-11-02', '2022-11-03'),
    (4, 1, 'cat', '??????4', '/files/2022/11/18/1c7a3ca1cff447b3abd33c5a90ee565c.jpg', '', '2022-11-02', '2022-11-03'),
    (5, 1, 'cat', '??????5', '/files/2022/11/18/1c7a3ca1cff447b3abd33c5a90ee565c.jpg', '', '2022-11-02', '2022-11-03')
ON DUPLICATE KEY UPDATE id = VALUES(id)
;

INSERT INTO category
(id, code, name, division, registered_at, updated_at)
VALUES
(1,   '101000000', '??????',      'MAIN',   '2022-11-22', '2022-11-22'),
(2,   '101001000', '?????????',    'MEDIUM', '2022-11-22', '2022-11-22'),
(3,   '101002000', '??????',      'MEDIUM', '2022-11-22', '2022-11-22'),
(4,   '101003000', '??????',      'MEDIUM', '2022-11-22', '2022-11-22'),
(5,   '101001001', '6????????????',  'SMALL',  '2022-11-22', '2022-11-22'),
(6,   '101001002', '12????????????', 'SMALL',  '2022-11-22', '2022-11-22'),
(7,   '101001003', '?????????',     'SMALL',  '2022-11-22', '2022-11-22'),

(8,   '102000000', '??????',      'MAIN',    '2022-11-22', '2022-11-22'),
(9,   '102001000', '??????',      'MEDIUM',  '2022-11-22', '2022-11-22'),
(10,  '102002000', '??????',      'MEDIUM',  '2022-11-22', '2022-11-22'),
(11,  '102001001', '??????/??????',  'SMALL',   '2022-11-22', '2022-11-22'),
(12,  '102002002', '??????',      'SMALL',   '2022-11-22', '2022-11-22'),
(13,  '102002003', '???',        'SMALL',   '2022-11-22', '2022-11-22'),

(14,  '103000000', '?????????',    'MAIN',   '2022-11-22', '2022-11-22'),
(15,  '103001000', '????????????',    'MEDIUM', '2022-11-22', '2022-11-22'),
(16,  '103002000', '????????????',   'MEDIUM', '2022-11-22', '2022-11-22'),
(17,  '103003000', '??????',      'MEDIUM', '2022-11-22', '2022-11-22'),
(18,  '101001001', '?????????',     'SMALL',  '2022-11-22', '2022-11-22'),
(19,  '101001002', '????????????',     'SMALL',  '2022-11-22', '2022-11-22'),
(20,  '101001003', '??????',  'SMALL',  '2022-11-22', '2022-11-22')

ON DUPLICATE KEY UPDATE id = VALUES(id)
;

INSERT INTO product
(id, category_main_code, category_medium_code, category_small_code, thumbnail_local_path, thumbnail_url_path, brand, name, registered_at, updated_at)
VALUES
(1,  '101000000', '101001000', '101001001', '', '/files/test/product01', '????????????', '???????????? 101',   '2022-11-11', '2022-12-11'),
(2,  '101000000', '101001000', '101001003', '', '/files/test/product02', '??????', '???????????? ????????????',   '2022-11-11', '2022-12-11'),
(3,  '101000000', '101001000', '101001001', '', '/files/test/product03', '??????', '???????????? ?????????',    '2022-11-11', '2022-12-11'),
(4,  '101000000', '101001000', '101001002', '', '/files/test/product04', '??????', '????????????',          '2022-11-11', '2022-12-11'),
(5,  '101000000', '101001000', '101001003', '', '/files/test/product01', '????????????', '???????????? 300',   '2022-11-11', '2022-12-11'),
(6,  '101000000', '101001000', '101001001', '', '/files/test/product02', '??????', '????????????',          '2022-11-11', '2022-12-11'),
(7,  '101000000', '101001000', '101001002', '', '/files/test/product03', '?????????', '???????????? ????????????', '2022-11-11', '2022-12-11'),
(8,  '101000000', '101001000', '101001003', '', '/files/test/product04', '?????????', '???????????? ????????????', '2022-11-11', '2022-12-11'),
(9,  '101000000', '101001000', '101001001', '', '/files/test/product01', '?????????', '???????????? ????????????', '2022-11-11', '2022-12-11'),
(10, '101000000', '101001000', '101001001', '', '/files/test/product02', '?????????', '???????????? ????????????', '2022-11-11', '2022-12-11'),
(11, '102000000', '102001000', '102001001', '', '/files/test/product02', '?????????', '???????????? ????????????', '2022-11-11', '2022-12-11'),
(12, '102000000', '102001000', '102001001', '', '/files/test/product02', '?????????', '???????????? ????????????', '2022-11-11', '2022-12-11'),
(13, '102000000', '102001000', '102001002', '', '/files/test/product02', '?????????', '???????????? ????????????', '2022-11-11', '2022-12-11'),
(14, '102000000', '102001000', '102001003', '', '/files/test/product02', '?????????', '???????????? ????????????', '2022-11-11', '2022-12-11'),
(15, '103000000', '103001000', '103001001', '', '/files/test/product02', '?????????', '???????????? ?????????',   '2022-11-11', '2022-12-11'),
(16, '103000000', '103001000', '103001001', '', '/files/test/product02', '?????????', '???????????? ?????????',   '2022-11-11', '2022-12-11'),
(17, '103000000', '103001000', '103001001', '', '/files/test/product02', '?????????', '???????????? ?????????',   '2022-11-11', '2022-12-11'),
(18, '103000000', '103001000', '103001001', '', '/files/test/product02', '?????????', '???????????? ?????????',   '2022-11-11', '2022-12-11'),
(19, '103000000', '103001000', '103001002', '', '/files/test/product02', '?????????', '???????????? ??????',     '2022-11-11', '2022-12-11'),
(20, '103000000', '103001000', '103001003', '', '/files/test/product02', '?????????', '???????????? ?????????',   '2022-11-11', '2022-12-11')
ON DUPLICATE KEY UPDATE id = VALUES(id)
;