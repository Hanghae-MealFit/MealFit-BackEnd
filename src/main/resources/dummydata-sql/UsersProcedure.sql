users# 더미 데이터 생성
DELIMITER $$
DROP PROCEDURE IF EXISTS loopInsertUser$$

CREATE PROCEDURE loopInsertUser()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 10000 DO
        INSERT INTO users(username , password, email, nickname, kcal, carbs, protein, fat, user_status, provider_type,
        start_fasting, end_fasting, goal_weight)
		VALUES
		(concat('user',i), concat('password',i), concat(concat('email',i), '@gmail.com'), concat('nickname', i), i, i, i, i, 'FIRST_LOGIN', 'LOCAL', '10:10:00', '15:10:00', i),
		(concat('user',i + 1), concat('password',i + 1), concat(concat('email',i + 1), '@gmail.com'), concat('nickname', i + 1), i + 1, i + 1, i + 1, i + 1, 'FIRST_LOGIN', 'LOCAL', '10:10:00', '15:10:00', i + 1),
		(concat('user',i + 2), concat('password',i + 2), concat(concat('email',i + 2), '@gmail.com'), concat('nickname', i + 2), i + 2, i + 2, i + 2, i + 2, 'FIRST_LOGIN', 'LOCAL', '10:10:00', '15:10:00', i + 2),
		(concat('user',i + 3), concat('password',i + 3), concat(concat('email',i + 3), '@gmail.com'), concat('nickname', i + 3), i + 3, i + 3, i + 3, i + 3, 'FIRST_LOGIN', 'LOCAL', '10:10:00', '15:10:00', i + 3),
		(concat('user',i + 4), concat('password',i + 4), concat(concat('email',i + 4), '@gmail.com'), concat('nickname', i + 4), i + 4, i + 4, i + 4, i + 4, 'FIRST_LOGIN', 'LOCAL', '10:10:00', '15:10:00', i + 4),
		(concat('user',i + 5), concat('password',i + 5), concat(concat('email',i + 5), '@gmail.com'), concat('nickname', i + 5), i + 5, i + 5, i + 5, i + 5, 'FIRST_LOGIN', 'LOCAL', '10:10:00', '15:10:00', i + 5),
		(concat('user',i + 6), concat('password',i + 6), concat(concat('email',i + 6), '@gmail.com'), concat('nickname', i + 6), i + 6, i + 6, i + 6, i + 6, 'FIRST_LOGIN', 'LOCAL', '10:10:00', '15:10:00', i + 6),
		(concat('user',i + 7), concat('password',i + 7), concat(concat('email',i + 7), '@gmail.com'), concat('nickname', i + 7), i + 7, i + 7, i + 7, i + 7, 'FIRST_LOGIN', 'LOCAL', '10:10:00', '15:10:00', i + 7),
		(concat('user',i + 8), concat('password',i + 8), concat(concat('email',i + 8), '@gmail.com'), concat('nickname', i + 8), i + 8, i + 8, i + 8, i + 8, 'FIRST_LOGIN', 'LOCAL', '10:10:00', '15:10:00', i + 8),
		(concat('user',i + 9), concat('password',i + 9), concat(concat('email',i + 9), '@gmail.com'), concat('nickname', i + 9), i + 9, i + 9, i + 9, i + 9, 'FIRST_LOGIN', 'LOCAL', '10:10:00', '15:10:00', i + 9);
        SET i = i + 10;
    END WHILE;
END$$
DELIMITER $$

call loopInsertUser();