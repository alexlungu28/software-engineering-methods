package roomscheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomscheduler.entities.IdNamePair;


@SpringBootTest
public class IdNamePairTest {

    @Test
    void constructorTest() {
        IdNamePair np = new IdNamePair(0, "test5");
        assertEquals(np.getId(), (Integer) 0);
        assertEquals(np.getName(), "test5");
    }

    @Test
    void toStringTest() {
        IdNamePair np = new IdNamePair(0, "test8");
        assertEquals(np.toString(), "IdNamePair{id=0, name='test8'}");
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
        IdNamePair np = new IdNamePair(0, "test90");
        IdNamePair np2 = new IdNamePair(0, "test90");
        assertNotSame(np, np2);
        assertEquals(np.hashCode(), np2.hashCode());
    }
}