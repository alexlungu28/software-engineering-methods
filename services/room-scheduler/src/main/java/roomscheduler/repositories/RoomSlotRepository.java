package roomscheduler.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomscheduler.entities.RoomSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomSlotRepository extends JpaRepository<RoomSlot, Integer> {



    @Query(value = "call add_room_slots(:numDailySlots, :numDays,:firstSlotDateTime," +
            ":timeSlotToSlot,:breakSlot,:numRooms)", nativeQuery = true)
    String createRoomSlots(@Param("numDailySlots") int numDailySlots, @Param("numDays") int numDays,
                           @Param("firstSlotDateTime") String firstSlotDateTime,
                           @Param("timeSlotToSlot") String timeSlotToSlot,
                           @Param("breakSlot") int breakSlot,
                           @Param("numRooms") long numRooms);
}
