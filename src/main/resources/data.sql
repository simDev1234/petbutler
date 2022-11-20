INSERT INTO user
    (id, user_roles, user_status, email, password,
     butler_level, phone, email_auth_yn, registered_at, updated_at)
VALUES
    (1, 'ROLE_ADMIN, ROLE_REGULAR', 'IN_USE', 'admin@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     2, '010-1234-5678', true, '2022-10-11', '2022-11-11'),
    (2, 'ROLE_ADMIN, ROLE_REGULAR', 'IN_USE', 'admin2@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
     2, '010-2345-5678', true, '2022-10-11', '2022-11-11'),
    (3, 'ROLE_ADMIN, ROLE_REGULAR', 'IN_USE', 'admin3@gmail.com', '$2a$10$EFGqEFJuPvOoUWr4AmyKD.iU5ys92tOqfBNSAmBsqcokqApV.eZVe',
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
    (1, 1, 'dog', '봄이1', '/files/2022/11/18/1c7a3ca1cff447b3abd33c5a90ee565c.jpg', '', '2022-11-02', '2022-11-03'),
    (2, 1, 'dog', '봄이2', '/files/2022/11/18/1c7a3ca1cff447b3abd33c5a90ee565c.jpg', '', '2022-11-02', '2022-11-03'),
    (3, 1, 'dog', '봄이3', '/files/2022/11/18/1c7a3ca1cff447b3abd33c5a90ee565c.jpg', '', '2022-11-02', '2022-11-03'),
    (4, 1, 'cat', '봄이4', '/files/2022/11/18/1c7a3ca1cff447b3abd33c5a90ee565c.jpg', '', '2022-11-02', '2022-11-03'),
    (5, 1, 'cat', '봄이5', '/files/2022/11/18/1c7a3ca1cff447b3abd33c5a90ee565c.jpg', '', '2022-11-02', '2022-11-03')
ON DUPLICATE KEY UPDATE id = VALUES(id)
;

INSERT INTO category
(id, code, name, division, registered_at, updated_at)
VALUES
(1,   '101000000', '주식',      'MAIN',   '2022-11-22', '2022-11-22'),
(2,   '101001000', '대용량',    'MEDIUM', '2022-11-22', '2022-11-22'),
(3,   '101002000', '중량',      'MEDIUM', '2022-11-22', '2022-11-22'),
(4,   '101003000', '소량',      'MEDIUM', '2022-11-22', '2022-11-22'),
(5,   '101001001', '6개월미만',  'SMALL',  '2022-11-22', '2022-11-22'),
(6,   '101001002', '12개월미만', 'SMALL',  '2022-11-22', '2022-11-22'),
(7,   '101001003', '전연령',     'SMALL',  '2022-11-22', '2022-11-22'),

(8,   '102000000', '간식',      'MAIN',    '2022-11-22', '2022-11-22'),
(9,   '102001000', '건식',      'MEDIUM',  '2022-11-22', '2022-11-22'),
(10,  '102002000', '습식',      'MEDIUM',  '2022-11-22', '2022-11-22'),
(11,  '102001001', '육류/생선',  'SMALL',   '2022-11-22', '2022-11-22'),
(12,  '102002002', '츄르',      'SMALL',   '2022-11-22', '2022-11-22'),
(13,  '102002002', '캔',        'SMALL',   '2022-11-22', '2022-11-22'),

(14,  '103000000', '의약품',    'MAIN',   '2022-11-22', '2022-11-22'),
(15,  '103001000', '대용량',    'MEDIUM', '2022-11-22', '2022-11-22'),
(16,  '103002000', '중량',      'MEDIUM', '2022-11-22', '2022-11-22'),
(17,  '101003000', '소량',      'MEDIUM', '2022-11-22', '2022-11-22'),
(18,  '101001001', '6개월미만',  'SMALL',  '2022-11-22', '2022-11-22'),
(19,  '101001002', '12개월미만', 'SMALL',  '2022-11-22', '2022-11-22'),
(20,  '101001003', '전연령',     'SMALL',  '2022-11-22', '2022-11-22')

ON DUPLICATE KEY UPDATE id = VALUES(id)
;

INSERT INTO product
(id, category_main_code, category_medium_code, category_small_code, thumbnail_local_path,
 thumbnail_url_path, brand, name, price, discount, stock, registered_at, updated_at)
VALUES
(1,  '101000000', '101001000', '101001001', '', '/files/test/product01', '로얄캐닌', '로얄캐닌 101',   21000, 19000, 21, '2022-11-11', '2022-12-11'),
(2,  '101000000', '101001000', '101001003', '', '/files/test/product02', '힐스', '직접만든 건식사료',   21000, 19000, 11, '2022-11-11', '2022-12-11'),
(3,  '101000000', '101001000', '101001001', '', '/files/test/product03', '패린', '저렴저렴 건식이',    21000, 19000, 31, '2022-11-11', '2022-12-11'),
(4,  '101000000', '101001000', '101001002', '', '/files/test/product04', '츄츄', '츄츄사료',          21000, 19000, 1, '2022-11-11', '2022-12-11'),
(5,  '101000000', '101001000', '101001003', '', '/files/test/product01', '보미엄마', '보미엄마 300',   21000, 19000, 0, '2022-11-11', '2022-12-11'),
(6,  '101000000', '101001000', '101001001', '', '/files/test/product02', '냠냠', '냠냠건식',          21000, 19000, 21, '2022-11-11', '2022-12-11'),
(7,  '101000000', '101001000', '101001002', '', '/files/test/product03', '풀무원', '우리아이 건식사료', 21000, 19000, 1, '2022-11-11', '2022-12-11'),
(8,  '101000000', '101001000', '101001003', '', '/files/test/product04', '펫월드', '배고플땐 건식사료', 21000, 19000, 4, '2022-11-11', '2022-12-11'),
(9,  '101000000', '101001000', '101001001', '', '/files/test/product01', '팻집사', '다이어트 전용사료', 21000, 19000, 8, '2022-11-11', '2022-12-11'),
(10, '101000000', '101001000', '101001002', '', '/files/test/product02', '팻집사', '영양보충 건식사료', 21000, 19000, 153, '2022-11-11', '2022-12-11')
ON DUPLICATE KEY UPDATE id = VALUES(id)
;