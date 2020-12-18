package project.op29sem58.courses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import project.op29sem58.courses.database.entities.Course;
import project.op29sem58.courses.database.entities.Lecture;

import java.time.LocalDate;

import static org.junit.Assert.*;

@SpringBootTest
public class LectureTest {

    private Course course;
    private LocalDate date;
    private int numberOfTimeslots;
    private int minNoStudents;
    private Lecture lecture;
    private Course exmpleCourse =  new Course("1", "ads", "Cse1305", 1);
    private static MockedStatic<LocalDate> mockedDate;

    @BeforeEach
    public void setUp() {
        this.course = exmpleCourse;
        this.date = LocalDate.of(2020, 12, 5);
        this.numberOfTimeslots = 5;
        this.minNoStudents = 50;
        lecture = new Lecture(course, date, numberOfTimeslots, minNoStudents);
    }

    @Test
    public void constructorTest() {
        assertNotNull(lecture);
        assertEquals(this.course,  exmpleCourse);
        assertEquals(this.date, LocalDate.of(2020, 12, 5));
        assertEquals(numberOfTimeslots, 5);
        assertEquals(minNoStudents, 50);
    }

    @Test
    public void getIdTest() {
        assertEquals(lecture.getId(), 0);
    }

    @Test
    public void setIdTest() {
        assertEquals(lecture.getId(), 0);
        lecture.setId(1);
        assertEquals(lecture.getId(), 1);
    }

    @Test
    public void getDate() {
        assertEquals(lecture.getDate(), LocalDate.of(2020, 12, 5));
    }

    @Test
    public void setDate() {
        assertEquals(lecture.getDate(), LocalDate.of(2020, 12, 5));
        lecture.setDate(LocalDate.of(2020, 12, 8));
        assertEquals(lecture.getDate(), LocalDate.of(2020, 12, 8));
    }

    @Test
    public void getNumberOfTimeslotsTest() {
        assertEquals(lecture.getNumberOfTimeslots(), 5);
    }

    @Test
    public void setNumberOfTimeslotsTest() {
        assertEquals(lecture.getNumberOfTimeslots(), 5);
        lecture.setNumberOfTimeslots(8);
        assertEquals(lecture.getNumberOfTimeslots(), 8);
    }

    @Test
    public void getCourseTest() {
        assertTrue(lecture.getCourse().equals(new Course("1", "ads", "Cse1305", 1)));
    }

    @Test
    public void setCourseTest() {
        assertTrue(lecture.getCourse().equals(new Course("1", "ads", "Cse1305", 1)));
        Course c = new Course("2", "AD", "CSE1333", 2);
        lecture.setCourse(c);
        assertTrue(lecture.getCourse().equals(c));
    }

    @Test
    public void getMinNoStudentsTest() {
        assertEquals(lecture.getMinNoStudents(), 50);
    }

    @Test
    public void setMinNoStudentsTest() {
        assertEquals(lecture.getMinNoStudents(), 50);
        lecture.setMinNoStudents(100);
        assertEquals(lecture.getMinNoStudents(), 100);
    }



}
