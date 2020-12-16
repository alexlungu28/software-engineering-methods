package roomscheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Timestamp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomscheduler.entities.RoomSlot;


@SpringBootTest
public class RoomSlotTest {

    @Test
    void constructorTest() {
        Timestamp d = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        RoomSlot nd = new RoomSlot(d, 0, 1);
        Timestamp ts = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        assertEquals(nd.getDate_time(), ts);
        assertEquals(nd.getOccupied(), (Integer) 0);
        assertEquals(nd.getRooms_id(), (Integer) 1);
        assertNull(nd.getId());
    }


    @Test
    void toStringTest() {
        Timestamp d = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        RoomSlot nd = new RoomSlot(d, 0, 1);
        nd.setId(1);
        assertEquals(nd.toString(), "RoomSlot{id=1, date_time=2020-12-01 " +
                "12:30:00.0, occupied=0, rooms_id=1}");
    }

    @Test
    void equalsTest() {
        Timestamp d = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        Timestamp d2 = new Timestamp(120, 11, 1, 12, 30, 0, 1);
        RoomSlot rs = new RoomSlot(d, 0, 1);
        RoomSlot rs2 = new RoomSlot(d, 0, 1);
        RoomSlot rs3 = new RoomSlot(d2, 0, 1);
        RoomSlot rs4 = new RoomSlot(d, 1, 1);
        final RoomSlot rs5 = new RoomSlot(d, 0, 2);
        assertEquals(rs, rs2);
        assertNotEquals(rs, rs3);
        assertNotEquals(rs, rs4);
        assertNotEquals(rs, rs5);
    }

    @Test
    public void testHashCode() {
        Timestamp d = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        RoomSlot rs = new RoomSlot(d, 0, 1);
        RoomSlot rs2 = new RoomSlot(d, 0, 1);
        assertNotSame(rs, rs2);
        assertEquals(rs.hashCode(), rs2.hashCode());
    }
}
