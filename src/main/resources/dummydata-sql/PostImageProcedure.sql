# post 더미 데이터 생성
DELIMITER $$
DROP PROCEDURE IF EXISTS loopInsertPostImage$$

CREATE PROCEDURE loopInsertPostImage()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 200000 DO
        INSERT INTO post_image(url, post_id)
		VALUES
		(concat('http://github.com/postImage',i), i),
		(concat('http://github.com/postImage',i + 1), i + 1),
		(concat('http://github.com/postImage',i + 2), i + 2),
		(concat('http://github.com/postImage',i + 3), i + 3),
		(concat('http://github.com/postImage',i + 4), i + 4),
		(concat('http://github.com/postImage',i + 5), i + 5),
		(concat('http://github.com/postImage',i + 6), i + 6),
		(concat('http://github.com/postImage',i + 7), i + 7),
		(concat('http://github.com/postImage',i + 8), i + 8),
		(concat('http://github.com/postImage',i + 9), i + 9);
        SET i = i + 10;
    END WHILE;
END$$
DELIMITER $$

call loopInsertPostImage();