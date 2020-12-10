package roomscheduler.entities;

import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity // This tells Hibernate to make a table out of this class
@Table(name = "room_slots")
public class RoomSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Timestamp date_time;
    private Integer occupied;
    private Integer rooms_id;


    protected RoomSlot() {

    }

    /**
     * Constructor for RoomSlot.
     *
     *
     * @param date_time date time
     * @param occupied boolean for whether the room slot is occupied or not
     * @param rooms_id room id
     */
    public RoomSlot(Timestamp date_time, Integer occupied, Integer rooms_id) {
        this.date_time = date_time;
        this.occupied = occupied;
        this.rooms_id = rooms_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getDate_time() {
        return date_time;
    }

    public void setDate_time(Timestamp date_time) {
        this.date_time = date_time;
    }

    public Integer getOccupied() {
        return occupied;
    }

    public void setOccupied(Integer occupied) {
        this.occupied = occupied;
    }

    public Integer getRooms_id() {
        return rooms_id;
    }

    public void setRooms_id(Integer rooms_id) {
        this.rooms_id = rooms_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomSlot roomSlot = (RoomSlot) o;
        return Objects.equals(id, roomSlot.id) &&
                Objects.equals(date_time, roomSlot.date_time) &&
                Objects.equals(occupied, roomSlot.occupied) &&
                Objects.equals(rooms_id, roomSlot.rooms_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date_time, occupied, rooms_id);
    }

    @Override
    public String toString() {
        return "RoomSlot{" +
                "id=" + id +
                ", date_time=" + date_time +
                ", occupied=" + occupied +
                ", rooms_id=" + rooms_id +
                '}';
    }
}

