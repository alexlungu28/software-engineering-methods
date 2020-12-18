package op29sem58.student.local.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import op29sem58.student.database.entities.RoomSchedule;
import org.junit.jupiter.api.Test;

public class LectureTest {
    @Test
    public void idTest() {
        RoomSchedule rs = new RoomSchedule();
        Lecture lecture = new Lecture(0, rs);
        assertEquals(0, lecture.getId());
        lecture.setId(1);
        assertEquals(1, lecture.getId());
    }

    @Test
    public void roomScheduleTest() {
        RoomSchedule rs = new RoomSchedule();
        Lecture lecture = new Lecture(0, rs);
        assertEquals(rs, lecture.getRoomSchedule());
        RoomSchedule rs2 = new RoomSchedule();
        lecture.setRoomSchedule(rs2);
        assertEquals(rs2, lecture.getRoomSchedule());
    }
}
