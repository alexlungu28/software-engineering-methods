package op29sem58.student.local.entities;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CourseTest {
    private Course getDummyCourse() {
        return new Course(
            0, new int[]{ 1, 2 }, "teacher0", "Course 1", "CSE0", 1
        );
    }

    @Test
    public void courseIdTest() {
        Course course = this.getDummyCourse();
        assertEquals(0, course.getCourseId());
        course.setCourseId(1);
        assertEquals(1, course.getCourseId());
    }

    @Test
    public void lectureIdsTest() {
        Course course = this.getDummyCourse();
        assertArrayEquals(new int[] {1, 2}, course.getLectureIds());
        course.setLectureIds(new int[]{2, 3});
        assertArrayEquals(new int[] {2, 3}, course.getLectureIds());
    }

    @Test
    public void teacherNetIdTest() {
        Course course = this.getDummyCourse();
        assertEquals("teacher0", course.getTeacherNetId());
        course.setTeacherNetId("teacher1");
        assertEquals("teacher1", course.getTeacherNetId());
    }

    @Test
    public void nameTest() {
        Course course = this.getDummyCourse();
        assertEquals("Course 1", course.getName());
        course.setName("Course 2");
        assertEquals("Course 2", course.getName());
    }

    @Test
    public void codeTest() {
        Course course = this.getDummyCourse();
        assertEquals("CSE0", course.getCode());
        course.setCode("CSE1");
        assertEquals("CSE1", course.getCode());
    }

    @Test
    public void yearOfStudyTest() {
        Course course = this.getDummyCourse();
        assertEquals(1, course.getYearOfStudy());
        course.setYearOfStudy(2);
        assertEquals(2, course.getYearOfStudy());
    }

    @Test
    public void courseHasLectureTest() {
        Course course = this.getDummyCourse();
        assertTrue(course.courseHasLecture(1));
        assertTrue(course.courseHasLecture(2));
        assertFalse(course.courseHasLecture(3));
    }
}
