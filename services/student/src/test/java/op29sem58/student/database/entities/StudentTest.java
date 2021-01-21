package op29sem58.student.database.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import op29sem58.student.database.repos.StudentEnrollmentRepo;
import op29sem58.student.local.entities.Course;
import op29sem58.student.local.entities.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class StudentTest {
    private static Student student;

    /**
     * To prevent code duplication in creating a Student.
     */
    @BeforeEach
    public void createStudent() {
        student = new Student(
                "Bas",
                LocalDateTime.now(),
                true);
    }

    @Test
    public void getLastVisitedTest() {
        LocalDateTime time1 = LocalDateTime.now();
        student.setLastVisited(time1);
        assertEquals(time1, student.getLastVisited());
        LocalDateTime time2 = LocalDateTime.now();
        student.setLastVisited(time2);
        assertEquals(time2, student.getLastVisited());
    }

    @Test
    public void netidTest() {
        String netid1 = "bert";
        student.setNetId(netid1);
        assertEquals(netid1, student.getNetId());
        String netid2 = "ernie";
        student.setNetId(netid2);
        assertEquals(netid2, student.getNetId());
    }

    @Test
    public void wantsToGoTest() {
        Boolean doesNotWant = false;
        student.setWantsToGo(doesNotWant);
        assertEquals(doesNotWant, student.getWantsToGo());
        Boolean doesWant = true;
        student.setWantsToGo(doesWant);
        assertEquals(doesWant, student.getWantsToGo());
    }

    @Test
    public void isEnrolledForTest() {
        // initialize courses
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(0, new int[]{2}, "teachernetid", "course0", "cse0", 1));
        courses.add(new Course(1, new int[]{3}, "teachernetid", "course1", "cse1", 1));

        List<StudentEnrollment> studentEnrollments = new ArrayList<>();
        StudentEnrollment studentEnrollment = new StudentEnrollment();
        studentEnrollment.setCourseId(0);
        studentEnrollment.setStudent(student);
        studentEnrollments.add(studentEnrollment);

        StudentEnrollmentRepo studentEnrollmentRepo = Mockito.mock(StudentEnrollmentRepo.class);
        Mockito.when(studentEnrollmentRepo
                .findByCourseIdAndStudent(0, student))
                .thenReturn(studentEnrollments);

        Lecture lecture1 = new Lecture(2, null);
        Lecture lecture2 = new Lecture(3, null);
        Lecture lecture3 = new Lecture(4, null);

        assertTrue(student.isEnrolledFor(courses, lecture1, studentEnrollmentRepo));
        assertFalse(student.isEnrolledFor(courses, lecture2, studentEnrollmentRepo));
        assertFalse(student.isEnrolledFor(courses, lecture3, studentEnrollmentRepo));
    }
}
