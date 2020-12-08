package roomscheduler.repositories;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomscheduler.entities.IntPair;
import roomscheduler.entities.RoomSchedule;
import roomscheduler.entities.RoomSlotStat;
import roomscheduler.entities.ScheduleInfo;

@Repository
public interface RoomScheduleRepository extends JpaRepository<RoomSchedule, Integer> {

    String selectRulesValue = "SELECT rules.value ";
    String fromRules = "FROM rules ";

    @Query(value = "SELECT a.date_time as date, a.rooms_id as roomId, a.id as roomSlotId " +
            "FROM room_slots AS a " +
            "JOIN room_slots AS b " +
            "ON b.rooms_id = a.rooms_id AND " +
            "timestampdiff(hour, a.date_time, b.date_time) = :numSlots " +
            "and DATE(a.date_time) = :date and " +
            "not (time(a.date_time) <= :lunchTime and time(b.date_time) > :lunchTime) " +
            "and not (a.occupied = 1 or b.occupied = 1) " +
            "ORDER BY a.rooms_id, a.date_time",
            nativeQuery = true)
    Iterable<RoomSlotStat> scheduleLecture(@Param("date") Date d,
                                           @Param("numSlots") Integer numSlots,
                                           @Param("lunchTime") Time lunchTime);

    @Query(value = "SELECT id as roomScheduleId, rooms_id as roomId, lectures_id as lectureId, " +
            "start_time as startTime, " +
            "ADDDATE(start_time, INTERVAL (numOfSlots * :slotDuration + (numOfSlots - 1) " +
            "* :breakDuration) MINUTE) " +
            "as endTime, rooms_id as coronaCapacity " +
            "FROM (SELECT r1.id as id, date_time as start_time, " +
            "lectures_id, rooms_id, " +
            "count(*) - 1 as numOfSlots" +
            " FROM rooms_schedule AS r1 " +
            "JOIN room_slots AS r2 " +
            "ON r1.room_slots_id = r2.id " +
            "group by r1.lectures_id) r3 ", nativeQuery = true)
    List<ScheduleInfo> getScheduleInfo(@Param("slotDuration") Integer slotDuration,
                                       @Param("breakDuration") Integer breakDuration);


    @Query(value = selectRulesValue +
            fromRules +
            "WHERE name = \"lunch slot\"",
            nativeQuery = true)
    Integer getLunchSlot();

    @Query(value = selectRulesValue +
            fromRules +
            "WHERE name = \"slot duration\"",
            nativeQuery = true)
    Integer getSlotDuration();

    @Query(value = selectRulesValue +
            fromRules +
            "WHERE name = \"break time\"",
            nativeQuery = true)
    Integer getBreakDuration();


    @Query(value = selectRulesValue +
            fromRules +
            "WHERE name = \"capacity at most 200\"",
            nativeQuery = true)
    Integer getMinPerc();

    @Query(value = selectRulesValue +
            fromRules +
            "WHERE name = \"capacity more than 200\"",
            nativeQuery = true)
    Integer getMaxPerc();

    @Query(value = "SELECT room_slots_id as RoomSlotId, id as RoomScheduleId " +
            "FROM rooms_schedule WHERE lectures_id = :lecture_id", nativeQuery = true)
    List<IntPair> getSlotIdAndRoomSlotId(@Param("lecture_id") Integer lecture_id);


    @Query(value = "SELECT room_slots_id " +
            "FROM rooms_schedule " +
            "WHERE year_of_study = :yearOfStudy",  nativeQuery = true)
    List<Integer> getSlotIdsForLecturesOfTheSameYear(@Param("yearOfStudy") Integer yearOfStudy);




}
