package roomscheduler;


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomscheduler.entities.RoomSchedule;


@SpringBootTest
public class RoomScheduleTest {

    @Test
    void constructorTest() {
        RoomSchedule rs = new RoomSchedule(1, 2, 1);
        assertEquals(rs.getRoom_slots_id(), (Integer) 1);
        assertEquals(rs.getLectures_id(), (Integer) 2);
        assertEquals(rs.getYear_of_study(), (Integer) 1);
        assertNull(rs.getId());
    }

    //    public RoomSchedule(Integer room_slots_id, Integer lectures_id, Integer year_of_study) {
    //        this.room_slots_id = room_slots_id;
    //        this.lectures_id = lectures_id;
    //        this.year_of_study = year_of_study;
    //    }

    @Test
    void toStringTest() {
        RoomSchedule rs = new RoomSchedule(1, 2, 1);
        assertEquals(rs.toString(), "RoomSchedule{id=null, room_slots_id=1, " +
                "lectures_id=2, year_of_study=1}");
    }

    @Test
    void equalsTest() {
        RoomSchedule rs = new RoomSchedule(1, 1, 1);
        RoomSchedule rs1 = new RoomSchedule(1, 1, 1);
        RoomSchedule rs2 = new RoomSchedule(2, 1, 1);
        RoomSchedule rs3 = new RoomSchedule(1, 2, 1);
        final RoomSchedule rs4 = new RoomSchedule(1, 1, 2);
        assertEquals(rs, rs1);
        assertNotEquals(rs, rs2);
        assertNotEquals(rs, rs3);
        assertNotEquals(rs, rs4);
    }

    @Test
    public void testHashCode() {
        RoomSchedule rs = new RoomSchedule(1, 1, 1);
        RoomSchedule rs1 = new RoomSchedule(1, 1, 1);
        assertNotSame(rs, rs1);
        assertEquals(rs.hashCode(), rs1.hashCode());
    }
}