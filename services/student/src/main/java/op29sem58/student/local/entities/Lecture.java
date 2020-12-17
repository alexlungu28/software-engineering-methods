package op29sem58.student.local.entities;

import op29sem58.student.database.entities.RoomSchedule;

/**
 * This class helps us temporary store the lectures to make it more easy to assign the students.
 */
public class Lecture {
    private RoomSchedule roomSchedule;
    private int id;

    public Lecture(int id, RoomSchedule roomSchedule) {
        this.id = id;
        this.roomSchedule = roomSchedule;
    }

    public RoomSchedule getRoomSchedule() {
        return this.roomSchedule;
    }
    
    public void setRoomSchedule(RoomSchedule roomSchedule) {
        this.roomSchedule = roomSchedule;
    }

    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
}
