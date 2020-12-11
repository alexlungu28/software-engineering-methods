package roomscheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.sql.Timestamp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomscheduler.entities.SlotInfo;


@SpringBootTest
public class SlotInfoTest {

    transient Timestamp time = new Timestamp(120, 9, 1, 12, 30, 0, 0);

    @Test
    void constructorTest() {
        SlotInfo si = new SlotInfo(time, 1, 1);
        Timestamp time2 = new Timestamp(120, 9, 1, 12, 30, 0, 0);
        assertEquals(si.getDate(), time2);
        assertEquals(si.getRoomId(), (Integer) 1);
        assertEquals(si.getRoomSlotId(), (Integer) 1);
    }

    @Test
    void equalsTest() {
        Timestamp time2 = new Timestamp(120, 10, 1, 12, 30, 0, 0);
        SlotInfo si = new SlotInfo(time, 1, 1);
        SlotInfo si2 = new SlotInfo(time, 1, 1);
        SlotInfo si3 = new SlotInfo(time2, 1, 1);
        SlotInfo si4 = new SlotInfo(time, 2, 1);
        final SlotInfo si5 = new SlotInfo(time, 1, 2);
        assertEquals(si, si2);
        assertNotEquals(si, si3);
        assertNotEquals(si, si4);
        assertNotEquals(si, si5);
    }

    @Test
    void toStringTest() {
        SlotInfo si = new SlotInfo(time, 1, 1);
        assertEquals(si.toString(), "SlotInfo{date=2020-10-01 12:30:00.0, roomId=1, roomSlotId=1}");
    }

    @Test
    public void testHashCode() {
        SlotInfo si = new SlotInfo(time, 1, 1);
        SlotInfo si2 = new SlotInfo(time, 1, 1);
        assertNotSame(si, si2);
        assertEquals(si.hashCode(), si2.hashCode());
    }
}
