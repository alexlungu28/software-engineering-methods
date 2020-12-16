package roomscheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.sql.Timestamp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomscheduler.entities.ScheduleInformation;




@SpringBootTest
public class ScheduleInformationTest {

    transient Timestamp start = new Timestamp(120, 9, 1, 12, 30, 0, 0);
    transient Timestamp start2 = new Timestamp(120, 9, 1, 12, 30, 0, 0);
    transient Timestamp end = new Timestamp(120, 11, 1, 12, 30, 0, 0);
    transient Timestamp end2 = new Timestamp(120, 11, 1, 12, 30, 0, 0);

    @Test
    void constructorTest() {
        ScheduleInformation si = new ScheduleInformation(1, start, end, 1, 1, 50);
        assertEquals(si.getRoomScheduleId(), (Integer) 1);
        assertEquals(si.getStartTime(), start2);
        assertEquals(si.getEndTime(), end2);
        assertEquals(si.getLectureId(), (Integer) 1);
        assertEquals(si.getRoomId(), (Integer) 1);
        assertEquals(si.getCoronaCapacity(), (Integer) 50);
    }

    @Test
    void equalsTest() {
        Timestamp start3 = new Timestamp(120, 10, 1, 12, 30, 0, 0);
        Timestamp end3 = new Timestamp(120, 12, 1, 12, 30, 0, 0);
        ScheduleInformation si = new ScheduleInformation(1, start, end, 1, 1, 50);
        ScheduleInformation si2 = new ScheduleInformation(1, start, end, 1, 1, 50);
        ScheduleInformation si3 = new ScheduleInformation(1, start3, end3, 1, 1, 50);
        ScheduleInformation si4 = new ScheduleInformation(1, start, end, 2, 2, 50);
        final ScheduleInformation si5 = new ScheduleInformation(2, start, end, 1, 1, 100);
        assertEquals(si, si2);
        assertNotEquals(si, si3);
        assertNotEquals(si, si4);
        assertNotEquals(si, si5);
    }

    @Test
    void toStringTest() {
        ScheduleInformation si = new ScheduleInformation(1, start, end, 1, 1, 50);
        assertEquals(si.toString(), "ScheduleInformation{roomScheduleId=1, " +
                "startTime=2020-10-01 12:30:00.0, endTime=2020-12-01 12:30:00.0, " +
                "lectureId=1, roomId=1, coronaCapacity=50}");
    }

    @Test
    public void testHashCode() {
        ScheduleInformation si = new ScheduleInformation(1, start, end, 1, 1, 50);
        ScheduleInformation si2 = new ScheduleInformation(1, start, end, 1, 1, 50);
        assertNotSame(si, si2);
        assertEquals(si.hashCode(), si2.hashCode());
    }

}
