package project.op29sem58.courses.buildpattern;

import java.time.LocalDate;
import project.op29sem58.courses.database.entities.Course;
import project.op29sem58.courses.database.entities.Lecture;


public class LectureBuilder implements Builder {
    private transient Course course;
    private transient LocalDate date;
    private transient int numberOfTimeslots;
    private transient int minNoStudents;

    public LectureBuilder(){}

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
        return new Lecture(course, date, minNoStudents, numberOfTimeslots);
    }

}
