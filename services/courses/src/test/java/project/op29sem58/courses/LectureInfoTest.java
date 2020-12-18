package project.op29sem58.courses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LectureInfoTest {

    private transient LectureInfo lectureInfo;


    @BeforeEach
    public void setUp() {
        lectureInfo = new LectureInfo(java.time.LocalDate.of(2020, 12, 12), 3, 2);
    }

    @Test
    public void test() {
        LectureInfo a = new LectureInfo();
        assertNotNull(a);
    }

    @Test
    public void notNull() {
        assertNotNull(lectureInfo);
    }

    @Test
    public void getCourseIdTest() {
        assertEquals(1, lectureInfo);
    }

    @Test
    public void getDateTest() {
        assertEquals(java.time.LocalDate.of(2020, 12, 12), lectureInfo.getDate());
    }

    @Test
    public void getNumOfSlotsTest() {
        assertEquals(3, lectureInfo.getNumberOfTimeslots());
    }

    @Test
    public void getMinNoStudentsTest() {
        assertEquals(2, lectureInfo.getMinNoStudents());
    }


    @Test
    public void setDateTest() {
        lectureInfo.setDate(java.time.LocalDate.of(2020, 11, 12));
        assertEquals(java.time.LocalDate.of(2020, 11, 12), lectureInfo.getDate());
    }

    @Test
    public void setNumOfSlotsTest() {
        lectureInfo.setNumberOfTimeslots(4);
        assertEquals(4, lectureInfo.getNumberOfTimeslots());
    }

    @Test
    public void setMinNoStudentsTest() {
        lectureInfo.setMinNoStudents(100);
        assertEquals(100, lectureInfo.getMinNoStudents());
    }

    @Test
    public void toStringTest() {
        assertEquals("LectureInfo{date=2020-12-12, nSlots=3, " +
                "minNoStudents=2}", lectureInfo.toString());
    }


}

