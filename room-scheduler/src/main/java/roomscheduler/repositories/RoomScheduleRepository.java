package roomscheduler.repositories;

import roomscheduler.entities.RoomSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomScheduleRepository extends JpaRepository<RoomSchedule, Integer> {
}
