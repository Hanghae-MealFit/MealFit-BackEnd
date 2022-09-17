# post 더미 데이터 생성
DELIMITER $$
DROP PROCEDURE IF EXISTS loopInsertPost$$

CREATE PROCEDURE loopInsertPost()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 200000 DO
        INSERT INTO post(content, view, like_it, user_id)
		VALUES
		(concat('content',i), i, i, FLOOR(1 + (RAND() * 10000))),
		(concat('content',i + 1), i + 1, i + 1, FLOOR(1 + (RAND() * 10000))),
		(concat('content',i + 2), i + 2, i + 2, FLOOR(1 + (RAND() * 10000))),
		(concat('content',i + 3), i + 3, i + 3, FLOOR(1 + (RAND() * 10000))),
		(concat('content',i + 4), i + 4, i + 4, FLOOR(1 + (RAND() * 10000))),
		(concat('content',i + 5), i + 5, i + 5, FLOOR(1 + (RAND() * 10000))),
		(concat('content',i + 6), i + 6, i + 6, FLOOR(1 + (RAND() * 10000))),
		(concat('content',i + 7), i + 7, i + 7, FLOOR(1 + (RAND() * 10000))),
		(concat('content',i + 8), i + 8, i + 8, FLOOR(1 + (RAND() * 10000))),
		(concat('content',i + 9), i + 9, i + 9, FLOOR(1 + (RAND() * 10000)));
        SET i = i + 10;
    END WHILE;
END$$
DELIMITER $$

call loopInsertPost();