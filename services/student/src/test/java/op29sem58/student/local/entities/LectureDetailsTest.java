package op29sem58.student.local.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class LectureDetailsTest {
    private LectureDetails getDummyLectureDetails(LocalDateTime startTime, LocalDateTime endTime) {
        return new LectureDetails(0, "Course 0", 1, true, startTime, endTime);
    }

    @Test
    public void lectureIdTest() {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime ldt2 = ldt.plusHours(2);
        LectureDetails ld = this.getDummyLectureDetails(ldt, ldt2);
        assertEquals(0, ld.getLectureId());
        ld.setLectureId(1);
        assertEquals(1, ld.getLectureId());
    }

    @Test
    public void courseNameTest() {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime ldt2 = ldt.plusHours(2);
        LectureDetails ld = this.getDummyLectureDetails(ldt, ldt2);
        assertEquals("Course 0", ld.getCourseName());
        ld.setCourseName("Course 1");
        assertEquals("Course 1", ld.getCourseName());
    }

    @Test
    public void roomIdTest() {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime ldt2 = ldt.plusHours(2);
        LectureDetails ld = this.getDummyLectureDetails(ldt, ldt2);
        assertEquals(1, ld.getRoomId());
        ld.setRoomId(2);
        assertEquals(2, ld.getRoomId());
    }

    @Test
    public void onCampusTest() {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime ldt2 = ldt.plusHours(2);
        LectureDetails ld = this.getDummyLectureDetails(ldt, ldt2);
        assertEquals(true, ld.isOnCampus());
        ld.setOnCampus(false);
        assertEquals(false, ld.isOnCampus());
    }

    @Test
    public void startTimeTest() {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime ldt2 = ldt.plusHours(2);
        LectureDetails ld = this.getDummyLectureDetails(ldt, ldt2);
        assertEquals(ldt, ld.getStartTime());
        ld.setStartTime(ldt2);
        assertEquals(ldt2, ld.getStartTime());
    }

    @Test
    public void endTimeTest() {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime ldt2 = ldt.plusHours(2);
        LectureDetails ld = this.getDummyLectureDetails(ldt, ldt2);
        assertEquals(ldt2, ld.getEndTime());
        ld.setEndTime(ldt);
        assertEquals(ldt, ld.getEndTime());
    }
}
