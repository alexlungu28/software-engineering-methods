CREATE DEFINER=`pu_op29sem58-2`@`%` PROCEDURE `add_rooms`()
BEGIN
declare i integer;
set i = 0;

label1: LOOP
    SET i = i + 1;
    IF i <= 20 THEN
      INSERT INTO `projects_op29sem58-2`.`rooms` (`name`, `capacity`) VALUES (concat('room_', i), 50);
	else
		leave label1;
    END IF;
  END LOOP label1;
SELECT 'Rooms Created';
END