package project.op29sem58.courses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.op29sem58.courses.buildpattern.Builder;
import project.op29sem58.courses.buildpattern.LectureBuilder;
import project.op29sem58.courses.buildpattern.Director;
import project.op29sem58.courses.database.entities.Course;
import project.op29sem58.courses.database.entities.Lecture;

import java.time.LocalDate;

public class LectureBuilderTest {

    private Builder lb;

    @BeforeEach
    void beforeAll(){
        lb = new LectureBuilder();
        lb.setNumberOfTimeslots(2);
        lb.setDate(LocalDate.now());
        lb.setCourse(new Course());
    }


    @Test
    void TestFirstYearLecture(){
        Director.constructFirstYear(lb);
        Lecture lecture = lb.build();
        assertEquals(lecture.getMinNoStudents(), 500);
    }

    @Test
    void TestSecondYearLecture(){
        Director.constructSecondYear(lb);
        Lecture lecture = lb.build();
        assertEquals(lecture.getMinNoStudents(), 350);
    }



}
