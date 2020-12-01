package op29sem58.room.repositories;

import op29sem58.room.entities.Room;
import op29sem58.room.entities.RoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {


    @Query(value = "call add_rooms", nativeQuery = true)
    String createInitialRooms();


    @Query(value = "SELECT id, name \n" +
            "FROM (SELECT id, name, \n" +
            "CASE \n" +
            "\tWHEN t1.capBool = 1 THEN FLOOR(t1.capacity * :maxPer/100)\n" +
            "    ELSE FLOOR(t1.capacity * :minPer/100)\n" +
            "END AS coronaCapacity \n" +
            "FROM (SELECT *,\n" +
            "\tCASE\n" +
            "\t\tWHEN capacity >= 100 THEN 1\n" +
            "\t\tELSE 0\n" +
            "\tEND AS capBool\n" +
            "\tFROM rooms) t1) t2\n" +
            "WHERE t2.coronaCapacity >= :numOfStudents",
            nativeQuery = true)
    List<RoomInfo> getRoomsWithCapacityAtLeast(@Param("numOfStudents") Integer numOfStudents,
                                               @Param("minPer") Integer minPer,
                                               @Param("maxPer") Integer maxPer);


}

