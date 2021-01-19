package project.op29sem58.courses.buildpattern;

import java.time.LocalDate;
import project.op29sem58.courses.LectureInfo;
import project.op29sem58.courses.database.entities.Course;
import project.op29sem58.courses.database.entities.Lecture;


public class LectureBuilder implements Builder {
    private transient Course course;
    private transient LocalDate date;
    private transient int numberOfTimeslots;
    private transient int minNoStudents;

    public LectureBuilder(){}

    /** A method that creates a lecture builder from a LectureInfo and Course object.
     *
     * @param lectureInfo this specifies the date, number of timeslots and number of students
     *                    for the lecture that is to be built
     * @param course this is the course the lecture to be built is for
     */
    public LectureBuilder(LectureInfo lectureInfo, Course course) {
        this.course = course;
        this.date = lectureInfo.getDate();
        this.numberOfTimeslots = lectureInfo.getNumberOfTimeslots();
        this.minNoStudents = lectureInfo.getMinNoStudents();
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setMinNoStudents(int minNoStudents) {
        this.minNoStudents = minNoStudents;
    }

    public void setNumberOfTimeslots(int numberOfTimeslots) {
        this.numberOfTimeslots = numberOfTimeslots;
    }

    public Lecture build() {
        return new Lecture(course, date, numberOfTimeslots, minNoStudents);
    }

    public static LectureBuilder of(LectureInfo lectureInfo, Course course) {
        return new LectureBuilder(lectureInfo, course);
    }

}
