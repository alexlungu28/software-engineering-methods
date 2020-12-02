CREATE DEFINER=`pu_qgnqKYYyW5Gw6`@`%` PROCEDURE `add_room_slots`(IN numDailySlots integer, IN numDays integer, 
IN firstSlotDateTime datetime, IN timeSlotToSlot time, IN breakSlot integer, IN numRooms integer)
BEGIN

declare countWorkDays integer;
declare countRooms integer;
declare countSlots integer;
declare slotDateTime datetime;
declare slotOccupied tinyint;

set countWorkDays = 1;
set countSlots = 1;
set countRooms  = 1;
set slotDateTime = firstSlotDateTime;


forDays: LOOP
	IF countWorkDays <= numDays  THEN
		IF DAYOFWEEK(slotDateTime) <> 1 AND DAYOFWEEK(slotDateTime) <> 7 THEN
			
            forDailySlots: LOOP
				IF countSlots <= numDailySlots THEN
					if countSlots <> breakSlot then
						set slotOccupied = 0;
					else
						set slotOccupied = 2;
					end if;
                
					forRooms: LOOP
						If countRooms <= numRooms then
							INSERT INTO `projects_op29sem58-room-schedule`.`room_slots` (`date_time`, `rooms_id`, `occupied`) VALUES (slotDateTime, countRooms, slotOccupied);
							set countRooms = countRooms + 1;
						else
							set countRooms = 1;
							leave forRooms;
						end if;
					END LOOP forRooms;
               
					set countSlots = countSlots + 1;
					set slotDateTime = addtime(slotDateTime, timeSlotToSlot);
				ELSE
					set countSlots = 1;
					leave forDailySlots;
				END IF;
			END LOOP forDailySlots;
        END IF;
		set slotDateTime = ADDDATE(firstSlotDateTime, INTERVAL countWorkDays DAY);
		set countWorkDays = countWorkDays + 1;
    ELSE
		leave forDays;
	END IF;
END LOOP forDays; 
SELECT 'Room Slots Created';
END