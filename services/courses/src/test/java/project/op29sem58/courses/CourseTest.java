package project.op29sem58.courses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import project.op29sem58.courses.database.entities.Course;

@SpringBootTest
public class CourseTest {

    private transient String teacherNetId;
    private transient String name;
    private transient String code;
    private transient int yearOfStudy;
    private transient Course course;
    private transient String ads = "ads";
    private transient String cc = "Cse1305";

    /**
     * Method that, before each test, creates an instance of a course.
     * This will will be used for testing.
     */
    @BeforeEach
    public void setUp() {
        this.teacherNetId = "1";
        this.name = ads;
        this.code = cc;
        this.yearOfStudy = 1;
        course = new Course(teacherNetId, name, code, yearOfStudy);
    }

    @Test
    public void constructorTest() {
        assertNotNull(course);
        assertEquals(course.getTeacherNetId(), "1");
        assertEquals(course.getName(), ads);
        assertEquals(course.getCode(), cc);
        assertEquals(course.getYearOfStudy(), 1);
    }

    @Test
    public void getIdTest() {
        assertEquals(course.getId(), 0);
    }

    @Test
    public void setIdTest() {
        assertEquals(course.getId(), 0);
        course.setId(1);
        assertEquals(course.getId(), 1);
    }

    @Test
    public void getTeacherNetIdTest() {
        assertEquals(course.getTeacherNetId(), "1");
    }

    @Test
    public void setTeacherNetIdTest() {
        assertEquals(course.getTeacherNetId(), "1");
        course.setTeacherNetId("2");
        assertEquals(course.getTeacherNetId(), "2");
    }

    @Test
    public void getNameTest() {
        assertEquals(course.getName(), ads);
    }

    @Test
    public void setNameTest() {
        assertEquals(course.getName(), ads);
        course.setName("AD");
        assertEquals(course.getName(), "AD");
    }

    @Test
    public void getCodeTest() {
        assertEquals(course.getCode(), cc);
    }

    @Test
    public void setCodeTest() {
        assertEquals(course.getCode(), cc);
        course.setCode("Cse1315");
        assertEquals(course.getCode(), "Cse1315");
    }

    @Test
    public void getYearsOfStudyTest() {
        assertEquals(course.getYearOfStudy(), 1);
    }

    @Test
    public void setYearsOfStudyTest() {
        assertEquals(course.getYearOfStudy(), 1);
        course.setYearOfStudy(2);
        assertEquals(course.getYearOfStudy(), 2);
    }

    @Test
    public void equalsTest() {
        Course c = new Course("1", ads, cc, 1);
        assertTrue(course.equals(c));
        Course c2 = new Course("2", "xx", cc, 1);
        assertFalse(course.equals(c2));
        Course c3 = new Course("1", "xx", cc, 1);
        assertFalse(course.equals(c3));
        Course c4 = new Course("1", ads, "xxx", 1);
        assertFalse(course.equals(c4));
        Course c5 = new Course("1", ads, "xx", 2);
        assertFalse(course.equals(c5));
    }

    @Test
    public void hashCodeTest() {
        Course c = new Course("1", ads, cc, 1);
        Assertions.assertEquals(c.hashCode(), c.hashCode());
    }

}
