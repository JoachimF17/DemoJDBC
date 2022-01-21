DELIMITER $$

CREATE PROCEDURE SP_INSERT_SECTION
    (IN section_id INT, IN section_name VARCHAR(50), IN delegate_id INT)
BEGIN
    INSERT INTO section VALUES (section_id, section_name, delegate_id);
END $$

DELIMITER ;