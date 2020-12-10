package op29sem58.room.repositories;

import java.util.List;
import op29sem58.room.entities.Room;
import op29sem58.room.entities.RoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {


    @Query(value = "call add_rooms", nativeQuery = true)
    String createInitialRooms();

    @Query(value = "SELECT \n" +
            "CASE \n" +
            "\tWHEN t1.capBool = 1 THEN FLOOR(t1.capacity * :maxPerc / 100)\n" +
            "    ELSE FLOOR(t1.capacity * :minPerc / 100)\n" +
            "END AS coronaCapacity \n" +
            "FROM (SELECT capacity, id,\n" +
            "\tCASE\n" +
            "\t\tWHEN capacity >= 200 THEN 1\n" +
            "\t\tELSE 0\n" +
            "\tEND AS capBool\n" +
            "\tFROM rooms) t1\n" +
            "    where id = :roomId", nativeQuery = true)
    Integer getCoronaCapacity(@Param("roomId") Integer roomId,
                              @Param("minPerc") Integer minPerc,
                              @Param("maxPerc") Integer maxPerc);


    @Query(value = "SELECT id, name \n" +
            "FROM (SELECT id, name, \n" +
            "CASE \n" +
            "\tWHEN t1.capBool = 1 THEN FLOOR(t1.capacity * :maxPer/100)\n" +
            "    ELSE FLOOR(t1.capacity * :minPer/100)\n" +
            "END AS coronaCapacity \n" +
            "FROM (SELECT *,\n" +
            "\tCASE\n" +
            "\t\tWHEN capacity >= 200 THEN 1\n" +
            "\t\tELSE 0\n" +
            "\tEND AS capBool\n" +
            "\tFROM rooms) t1) t2\n" +
            "WHERE t2.coronaCapacity >= :numOfStudents",
            nativeQuery = true)
    List<RoomInfo> getRoomsWithCapacityAtLeast(@Param("numOfStudents") Integer numOfStudents,
                                               @Param("minPer") Integer minPer,
                                               @Param("maxPer") Integer maxPer);


}

