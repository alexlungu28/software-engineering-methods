package op29sem58.room.repositories;

import op29sem58.room.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {


    @Query(value = "call add_rooms", nativeQuery = true)
    String createInitialRooms();


}

