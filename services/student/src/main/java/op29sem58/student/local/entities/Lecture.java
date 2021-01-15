package op29sem58.student.local.entities;

import lombok.Getter;
import lombok.Setter;
import op29sem58.student.database.entities.RoomSchedule;

/**
 * This class helps us temporary store the lectures to make it more easy to assign the students.
 */
@Getter
@Setter
public class Lecture {
    private int id;
    private RoomSchedule roomSchedule;

    public Lecture(int id, RoomSchedule roomSchedule) {
        this.id = id;
        this.roomSchedule = roomSchedule;
    }
}
