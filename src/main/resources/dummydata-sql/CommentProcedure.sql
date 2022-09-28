# comment 더미 데이터 생성
DELIMITER $$
DROP PROCEDURE IF EXISTS loopInsertComment$$

CREATE PROCEDURE loopInsertComment()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 500000 DO
        INSERT INTO comment(content, post_id , user_id, like_it)
		VALUES
		(concat('content',i), FLOOR(1 + (RAND() * 10000)), FLOOR(1 + (RAND() * 10000)), i),
		(concat('content',i + 1), FLOOR(1 + (RAND() * 10000)), FLOOR(1 + (RAND() * 10000)), i),
		(concat('content',i + 1), FLOOR(1 + (RAND() * 10000)), FLOOR(1 + (RAND() * 10000)), i),
		(concat('content',i + 2), FLOOR(1 + (RAND() * 10000)), FLOOR(1 + (RAND() * 10000)), i),
		(concat('content',i + 3), FLOOR(1 + (RAND() * 10000)), FLOOR(1 + (RAND() * 10000)), i),
		(concat('content',i + 4), FLOOR(1 + (RAND() * 10000)), FLOOR(1 + (RAND() * 10000)), i),
		(concat('content',i + 5), FLOOR(1 + (RAND() * 10000)), FLOOR(1 + (RAND() * 10000)), i),
		(concat('content',i + 6), FLOOR(1 + (RAND() * 10000)), FLOOR(1 + (RAND() * 10000)), i),
		(concat('content',i + 7), FLOOR(1 + (RAND() * 10000)), FLOOR(1 + (RAND() * 10000)), i),
		(concat('content',i + 8), FLOOR(1 + (RAND() * 10000)), FLOOR(1 + (RAND() * 10000)), i),
		(concat('content',i + 9), FLOOR(1 + (RAND() * 10000)), FLOOR(1 + (RAND() * 10000)), i);
        SET i = i + 10;
    END WHILE;
END$$
DELIMITER $$

call loopInsertComment();