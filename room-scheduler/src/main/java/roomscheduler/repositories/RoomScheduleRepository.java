package roomscheduler.repositories;

import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomscheduler.entities.DateIntPair;
import roomscheduler.entities.RoomSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomscheduler.entities.RoomSlot;
import roomscheduler.entities.RoomSlotStat;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Repository
public interface RoomScheduleRepository extends JpaRepository<RoomSchedule, Integer> {


    @Query(value = "SELECT a.date_time as date, a.rooms_id as roomId " +
            "FROM room_slots AS a " +
            "JOIN room_slots AS b " +
            "ON b.rooms_id = a.rooms_id AND timestampdiff(hour, a.date_time, b.date_time) = :numSlots " +
            "and DATE(a.date_time) = :date and not (time(a.date_time) <= :lunchTime and time(b.date_time) > :lunchTime) " +
            "and not (a.occupied = 1 or b.occupied = 1) " +
            "ORDER BY a.rooms_id, a.date_time",
            nativeQuery = true)
    Iterable<RoomSlotStat> scheduleLecture(@Param("date") Date d,
                                           @Param("numSlots") Integer numSlots,
                                           @Param("lunchTime") Time lunchTime);


    @Query(value = "SELECT rules.value " +
            "FROM rules " +
            "WHERE name = \"lunch slot\"",
            nativeQuery = true)
    Integer getLunchSlot();


    @Query(value = "SELECT rules.value " +
            "FROM rules " +
            "WHERE name = \"capacity at most 200\"",
            nativeQuery = true)
    Integer getMinPerc();

    @Query(value = "SELECT rules.value " +
            "FROM rules " +
            "WHERE name = \"capacity more than 200\"",
            nativeQuery = true)
    Integer getMaxPerc();

}
