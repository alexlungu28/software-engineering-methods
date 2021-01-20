package project.op29sem58.courses;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.op29sem58.courses.buildpattern.Builder;
import project.op29sem58.courses.buildpattern.Director;
import project.op29sem58.courses.buildpattern.LectureBuilder;
import project.op29sem58.courses.database.entities.Course;
import project.op29sem58.courses.database.entities.Lecture;


public class LectureBuilderTest {

    private transient Builder lb;

    @BeforeEach
    void beforeAll() {
        lb = new LectureBuilder();
        lb.setNumberOfTimeslots(2);
        lb.setDate(LocalDate.now());
        lb.setCourse(new Course());
    }


    @Test
    void testFirstYearLecture() {
        Director.constructFirstYear(lb);
        Lecture lecture = lb.build();
        assertEquals(lecture.getMinNoStudents(), 500);
    }

    @Test
    void testSecondYearLecture() {
        Director.constructSecondYear(lb);
        Lecture lecture = lb.build();
        assertEquals(lecture.getMinNoStudents(), 350);
    }



}
