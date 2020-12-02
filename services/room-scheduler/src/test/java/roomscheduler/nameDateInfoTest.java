package roomscheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomscheduler.entities.NameDateInfo;
import roomscheduler.entities.RoomSlot;

import java.sql.Timestamp;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotSame;

@SpringBootTest
public class nameDateInfoTest {

    @Test
    void contextLoads() {
    }

    @Test
    void constructorTest() {
        Timestamp d = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        NameDateInfo nd = new NameDateInfo(d, "test", 1);
        Timestamp ts = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        assertEquals(nd.getDate(), ts);
        assertEquals(nd.getRoomName(), "test");
        assertEquals(nd.getRoomSlotId(),(Integer) 1);
    }

    @Test
    void toStringTest() {
        Timestamp d = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        NameDateInfo nd = new NameDateInfo(d, "test", 1);
        assertEquals(nd.toString(), "NameDateInfo{date=2020-12-01 12:30:00.0, roomName='test', roomSlotId=1}");

    }

    @Test
    void equalsTest() {
        Timestamp d = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        Timestamp d2 = new Timestamp(120, 11, 1, 12, 30, 0, 1);
        NameDateInfo nd = new NameDateInfo(d, "test", 1);
        NameDateInfo nd2 = new NameDateInfo(d, "test", 1);
        NameDateInfo nd3 = new NameDateInfo(d2, "test", 1);
        NameDateInfo nd4 = new NameDateInfo(d, "test1111", 1);
        NameDateInfo nd5 = new NameDateInfo(d, "test", 2);
        assertEquals(nd, nd2);
        assertNotEquals(nd, nd3);
        assertNotEquals(nd, nd4);
        assertNotEquals(nd, nd5);
    }

    @Test
    public void testHashCode() {
        Timestamp d = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        NameDateInfo nd = new NameDateInfo(d, "test", 1);
        NameDateInfo nd2 = new NameDateInfo(d, "test", 1);
        assertNotSame(nd, nd2);
        assertEquals(nd.hashCode(), nd2.hashCode());
    }
}
