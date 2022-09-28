# 더미 데이터 생성
DELIMITER $$
DROP PROCEDURE IF EXISTS loopInsertFood$$

CREATE PROCEDURE loopInsertFood()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 1000000 DO
        INSERT INTO food(food_name , made_by, one_serving, kcal, carbs, protein, fat)
		VALUES
		(concat('food',i), concat('제조사',i), i, i, i, i, i),
		(concat('food',i + 1), concat('제조사',i + 1), i + 1, i + 1, i + 1, i + 1, i + 1),
		(concat('food',i + 1), concat('제조사',i + 1), i + 1, i + 1, i + 1, i + 1, i + 1),
		(concat('food',i + 2), concat('제조사',i + 2), i + 2, i + 2, i + 2, i + 2, i + 2),
		(concat('food',i + 3), concat('제조사',i + 3), i + 3, i + 3, i + 3, i + 3, i + 3),
		(concat('food',i + 4), concat('제조사',i + 4), i + 4, i + 4, i + 4, i + 4, i + 4),
		(concat('food',i + 5), concat('제조사',i + 5), i + 5, i + 5, i + 5, i + 5, i + 5),
		(concat('food',i + 6), concat('제조사',i + 6), i + 6, i + 6, i + 6, i + 6, i + 6),
		(concat('food',i + 7), concat('제조사',i + 7), i + 7, i + 7, i + 7, i + 7, i + 7),
		(concat('food',i + 8), concat('제조사',i + 8), i + 8, i + 8, i + 8, i + 8, i + 8),
		(concat('food',i + 9), concat('제조사',i + 9), i + 9, i + 9, i + 9, i + 9, i + 9);
        SET i = i + 10;
    END WHILE;
END$$
DELIMITER $$

call loopInsertFood();