package roomscheduler.repositories;

import roomscheduler.entities.RoomSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomSlotRepository extends JpaRepository<RoomSlot, Integer> {
}
