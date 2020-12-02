package roomscheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomscheduler.entities.IdNamePair;
import roomscheduler.entities.NameDateInfo;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IdNamePairTest {

    @Test
    void constructorTest() {
        IdNamePair np = new IdNamePair(0, "test");
        assertEquals(np.getId(), (Integer) 0);
        assertEquals(np.getName(), "test");
    }

    @Test
    void toStringTest() {
        IdNamePair np = new IdNamePair(0, "test");
        assertEquals(np.toString(), "IdNamePair{id=0, name='test'}");
    }

    @Test
    void equalsTest() {
        IdNamePair np = new IdNamePair(0, "test");
        IdNamePair np2 = new IdNamePair(0, "test");
        IdNamePair np3 = new IdNamePair(1, "test");
        IdNamePair np4 = new IdNamePair(0, "test1");
        assertEquals(np, np2);
        assertNotEquals(np, np3);
        assertNotEquals(np, np4);
    }

    @Test
    public void testHashCode() {
        IdNamePair np = new IdNamePair(0, "test");
        IdNamePair np2 = new IdNamePair(0, "test");
        assertNotSame(np, np2);
        assertEquals(np.hashCode(), np2.hashCode());
    }
}