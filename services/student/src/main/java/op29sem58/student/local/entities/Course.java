package op29sem58.student.local.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * This Class is created so that we can store the list we receive from the courses service,
 * which are only the courses with their lectures. This helps with assigning the students
 * as we can check if they are enrolled for the course.
 */
@Getter
@Setter
public class Course {
    private int courseId;
    private String teacherNetId;
    private String name;
    private String code;
    private int yearOfStudy;
    private int[] lectureIds;

    /**
     * Basis constructor to create a course object.
     *
     * @param courseId Well the courseId.
     * @param lectureIds The Id's of the lectures
     * @param teacherNetId the NetId of the teacher
     * @param name the name of the course
     * @param code the course code
     * @param yearOfStudy For what year this course is held.
     */
    public Course(int courseId, int[] lectureIds, String teacherNetId, String name, String code,
                  int yearOfStudy) {
        this.courseId = courseId;
        this.lectureIds = lectureIds;
        this.teacherNetId = teacherNetId;
        this.name = name;
        this.code = code;
        this.yearOfStudy = yearOfStudy;
    }

    /**
     * If the course has this lectureId, it will return true.
     *
     * @param lectureId to check if the course has this lecture
     * @return true if course contains this lecture, false otherwise
     */
    public boolean courseHasLecture(int lectureId) {
        for (int id : this.lectureIds) {
            if (id == lectureId) {
                return true;
            }
        }
        return false;
    }
}
