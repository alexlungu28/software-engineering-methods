package op29sem58.student.database.repos;

import op29sem58.student.database.entities.RoomSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentBookingRepo extends JpaRepository<RoomSchedule, Integer> { }
