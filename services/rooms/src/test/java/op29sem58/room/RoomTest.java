package op29sem58.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import op29sem58.room.entities.Room;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoomTest {
    /**
     * Testing the entity Room's constructor method.
     * Checking to see if a Room instance returns an exception if a negative capacity is inputted.
     */
    @Test
    public void roomNegativeCapacityTest() {
        try {
            new Room("tz5", -100);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "invalid capacity");
        }
    }

    /**
     * Testing the entity Room's constructor method.
     * Checking to see if a Room instance can be created and if we can return the Name and capacity.
     */
    @Test
    public void roomConstructorTest() {
        Room room = new Room("tz8", 100);
        assertEquals(room.getName(), "tz8");
        assertEquals(room.getCapacity(), (Integer) 100);
    }


    @Test
    public void setCapacityTest() {
        try {
            Room r = new Room("tz5", 200);
            r.setCapacity(-1150);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "invalid capacity");
        }
    }

    @Test
    public void nullEqualsTest() {
        Room r = new Room("tz7", 200);
        Object b = null;
        assertTrue(!r.equals(b));
    }

    @Test
    public void equalsTestSameObject() {
        Room r = new Room("tz5", 200);
        assertEquals(r, r);
    }





    @Test
    public void testEqualsSymmetric() {
        Room x = new Room("a", 10);
        Room y = new Room("a", 10);
        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
    }

    /**
     * Testing the entity Room's equal method.
     * Checking to see if two instances of the same Room return true.
     */
    @Test
    public void roomEquelsTest() {
        Room room1 = new Room("tz2", 100);
        Room room2 = new Room("tz2", 100);
        Room room3 = new Room("tz3", 100);
        Room room4 = new Room("tz2", 130);
        //test two instances of same room.
        assertTrue(room1.equals(room2));
        //test two instances of room with different name.
        assertFalse(room2.equals(room3));
        //test two instances of room with different capacity.
        assertFalse(room2.equals(room4));
    }
}
