package op29sem58.student;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class CourseLecturesTest {
    @Test
    public void constructorTest() {
        var courseLectures = new CourseLectures(1, new int[]{});
        assertNotNull(courseLectures);
    }
}
