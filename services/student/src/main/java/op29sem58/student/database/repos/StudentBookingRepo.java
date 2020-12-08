package op29sem58.student.database.repos;

import op29sem58.student.database.entities.RoomSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is some basic code to let hibernate know we want a repository for the StudentBookings, and that we can retrieve
 * them.
 */
@Repository
public interface StudentBookingRepo extends JpaRepository<RoomSchedule, Integer> { }
