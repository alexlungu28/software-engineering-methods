package op29sem58.student.local.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UserPreferenceTest {
    @Test
    public void wantsToGoTest() {
        UserPreference up = new UserPreference("student0", true);
        assertTrue(up.isWantsToGo());
        up.setWantsToGo(false);
        assertFalse(up.isWantsToGo());
    }

    @Test
    public void studentIdTest() {
        UserPreference up = new UserPreference("student0", true);
        assertEquals("student0", up.getStudentId());
        up.setStudentId("student1");
        assertEquals("student1", up.getStudentId());
    }
}
