package roomscheduler.entities;

import java.util.Objects;
import javax.persistence.*;


@Entity // This tells Hibernate to make a table out of this class
@Table(name = "rooms_schedule")
public class RoomSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer room_slots_id;
    private Integer lectures_id;
    private Integer year_of_study;


    protected RoomSchedule() {

    }


    /**
     * Constructor for RoomSchedule.
     *

     *

     * @param room_slots_id
     * @param lectures_id
     * @param year_of_study
     */
    public RoomSchedule(Integer room_slots_id, Integer lectures_id, Integer year_of_study) {
        this.room_slots_id = room_slots_id;
        this.lectures_id = lectures_id;
        this.year_of_study = year_of_study;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoom_slots_id() {
        return room_slots_id;
    }

    public void setRoom_slots_id(Integer room_slots_id) {
        this.room_slots_id = room_slots_id;
    }

    public Integer getLectures_id() {
        return lectures_id;
    }

    public void setLectures_id(Integer lectures_id) {
        this.lectures_id = lectures_id;
    }

    public Integer getYear_of_study() {
        return year_of_study;
    }

    public void setYear_of_study(Integer year_of_study) {
        this.year_of_study = year_of_study;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomSchedule that = (RoomSchedule) o;
        return Objects.equals(id, that.id)
                && Objects.equals(room_slots_id, that.room_slots_id)
                && Objects.equals(lectures_id, that.lectures_id)
                && Objects.equals(year_of_study, that.year_of_study);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, room_slots_id, lectures_id, year_of_study);
    }

    @Override
    public String toString() {
        return "RoomSchedule{"
                + "id=" + id
                + ", room_slots_id=" + room_slots_id
                + ", lectures_id=" + lectures_id
                + ", year_of_study=" + year_of_study
                + '}';
    }
}
