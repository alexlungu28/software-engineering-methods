package roomscheduler;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.sql.Timestamp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomscheduler.entities.NameDateInfo;


@SpringBootTest
public class NameDateInfoTest {

    @Test
    void contextLoads() {
    }

    @Test
    void constructorTest() {
        Timestamp d = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        NameDateInfo nd = new NameDateInfo(d, "test5", 1);
        Timestamp ts = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        assertEquals(nd.getDate(), ts);
        assertEquals(nd.getRoomName(), "test5");
        assertEquals(nd.getRoomSlotId(), (Integer) 1);
    }

    @Test
    void toStringTest() {
        Timestamp d = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        NameDateInfo nd = new NameDateInfo(d, "test8", 1);
        assertEquals(nd.toString(), "NameDateInfo{date=2020-12-01 12:30:00.0, " +
                "roomName='test8', roomSlotId=1}");

    }

    @Test
    void equalsTest() {
        Timestamp d = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        Timestamp d2 = new Timestamp(120, 11, 1, 12, 30, 0, 1);
        NameDateInfo nd = new NameDateInfo(d, "test98", 1);
        NameDateInfo nd2 = new NameDateInfo(d, "test98", 1);
        NameDateInfo nd3 = new NameDateInfo(d2, "test", 1);
        NameDateInfo nd4 = new NameDateInfo(d, "test1111", 1);
        final NameDateInfo nd5 = new NameDateInfo(d, "test", 2);
        assertEquals(nd, nd2);
        assertNotEquals(nd, nd3);
        assertNotEquals(nd, nd4);
        assertNotEquals(nd, nd5);
    }

    @Test
    public void testHashCode() {
        Timestamp d = new Timestamp(120, 11, 1, 12, 30, 0, 0);
        NameDateInfo nd = new NameDateInfo(d, "test90", 1);
        NameDateInfo nd2 = new NameDateInfo(d, "test90", 1);
        assertNotSame(nd, nd2);
        assertEquals(nd.hashCode(), nd2.hashCode());
    }
}
