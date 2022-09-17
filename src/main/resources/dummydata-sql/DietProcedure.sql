users# 더미 데이터 생성
DELIMITER $$
DROP PROCEDURE IF EXISTS loopInsertFood$$

CREATE PROCEDURE loopInsertDiet()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 100000 DO
        INSERT INTO diet(food_id , food_weight, status, user_id, diet_date)
		VALUES
		(concat('diet',i), concat('제조사',i), i, i, i, i, i),
		(concat('diet',i + 1), concat('제조사',i + 1), i + 1, i + 1, '2022-09-01'),
		(concat('diet',i + 1), concat('제조사',i + 1), i + 1, i + 1, '2022-09-01'),
		(concat('diet',i + 2), concat('제조사',i + 2), i + 2, i + 2, '2022-09-01'),
		(concat('diet',i + 3), concat('제조사',i + 3), i + 3, i + 3, '2022-09-01'),
		(concat('diet',i + 4), concat('제조사',i + 4), i + 4, i + 4, '2022-09-01'),
		(concat('diet',i + 5), concat('제조사',i + 5), i + 5, i + 5, '2022-09-01'),
		(concat('diet',i + 6), concat('제조사',i + 6), i + 6, i + 6, '2022-09-01'),
		(concat('diet',i + 7), concat('제조사',i + 7), i + 7, i + 7, '2022-09-01'),
		(concat('diet',i + 8), concat('제조사',i + 8), i + 8, i + 8, '2022-09-01'),
		(concat('diet',i + 9), concat('제조사',i + 9), i + 9, i + 9, '2022-09-01');
        SET i = i + 10;
    END WHILE;
END$$
DELIMITER $$

call loopInsertDiet();