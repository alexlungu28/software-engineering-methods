package op29sem58.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import op29sem58.room.entities.IdNamePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IdNamePairTest {

    transient private IdNamePair idNamePair;
    transient private String name;

    /**
     * Method that, before each test, creates an IdNamePair
     * instance used for testing.
     */
    @BeforeEach
    public void setUp() {
        this.name = "name";
        idNamePair = new IdNamePair(1, name);
    }

    @Test
    public void constructorTest() {
        assertNotNull(idNamePair);
    }

    @Test
    public void getIdTest() {
        assertEquals(1, idNamePair.getId());

    }

    @Test
    public void setIdTest() {
        idNamePair.setId(2);
        assertEquals(2, idNamePair.getId());
    }

    @Test
    public void getNameTest() {
        assertEquals(name, idNamePair.getName());
    }

    @Test
    public void setNameTest() {
        idNamePair.setName("name2");
        assertEquals("name2", idNamePair.getName());
    }

    @Test
    public void equalsTest() {
        assertTrue(idNamePair.equals(idNamePair));
    }

    @Test
    public void equalsNullTest() {
        Object n = null;
        assertTrue(!idNamePair.equals(n));
    }

    @Test
    public void equalsDifferentObjectTest() {
        String b = "b";
        assertTrue(!idNamePair.equals(b));
    }

    @Test
    public void equalsTestDifferent() {
        IdNamePair b = new IdNamePair(3, name);
        assertTrue(!idNamePair.equals(b));
    }

    @Test
    public void hashCodeTest() {
        IdNamePair c = new IdNamePair(1, name);
        assertEquals(c.hashCode(), idNamePair.hashCode());
    }

    @Test
    public void equalsTest2() {
        IdNamePair c = new IdNamePair(1, "name2");
        assertTrue(!c.equals(idNamePair));
    }

    @Test
    public void equalsTest3() {
        IdNamePair c = new IdNamePair(1, name);
        assertTrue(c.equals(idNamePair));
    }

    @Test
    public void toStringTest() {
        assertEquals("IdNamePair{id=1, name='name'}",
                idNamePair.toString());
    }
}
